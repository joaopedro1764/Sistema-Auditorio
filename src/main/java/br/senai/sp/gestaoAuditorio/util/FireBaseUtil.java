package br.senai.sp.gestaoAuditorio.util;

import java.io.IOException;
import java.util.UUID;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

@Service
public class FireBaseUtil {
	// variável para guardar as credenciais do Firebase
	private Credentials credenciais;
	// variável para acessar o storage
	private Storage storage;
	// constante para o nome do Bucket
	private final String BUCKET_NAME = "sistemaauditoriotarde.appspot.com";
	// constante para o prefixo da url
	private final String PREFIX = "https://firebasestorage.googleapis.com/v0/b/"+BUCKET_NAME+"/o/";
	// constante para o sufixo da URL
	private final String SUFFIX = "?alt=media";
	// constante para a URL
	private final String DOWNLOAD_URL = PREFIX + "%s" + SUFFIX;

	public FireBaseUtil() { // construtor
		// buscar as credenciais (arquivo JSON)
		Resource resources = new ClassPathResource("chavefirebase.json");
		try {
			// ler o arquivo para obter as credenciais
			credenciais = GoogleCredentials.fromStream(resources.getInputStream());
			// acessa o serviço de storage
			storage = StorageOptions.newBuilder().setCredentials(credenciais).build().getService();
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	public String uploadFile(MultipartFile arquivo) throws IOException {
		// gera uma String aleatória para o nome do arquivo
		String nomeArquivo = UUID.randomUUID().toString() + getExtensao(arquivo.getOriginalFilename());
		// criar um BlobId
		BlobId blobId = BlobId.of(BUCKET_NAME, nomeArquivo);
		// criat um blobInfo a partir do BlobId
		BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("media").build();
		// manda o blobInfo para o Storage passando os bytes do arquivo para ele
		storage.create(blobInfo, arquivo.getBytes());
		// retornar a URL para acessar o arquivo
		return String.format(DOWNLOAD_URL, nomeArquivo);
	}
	
	private String getExtensao(String nomeArquivo) {
		// retorna o trecho da String que vai do último ponto até o fim
		return nomeArquivo.substring(nomeArquivo.lastIndexOf('.'));
	}
	
	// método para excluir a foto do FireBase
	public void deletar(String nomeArquivo) {
		// retira o prefixo e o sufixo do nome do arquivo
		nomeArquivo = nomeArquivo.replace(PREFIX, "").replace(SUFFIX, "");
		// pega um blob através do nome do arquivo
		Blob blob = storage.get(BlobId.of(BUCKET_NAME, nomeArquivo));
		// deleta o arquivo
		storage.delete(blob.getBlobId());
	}
}
