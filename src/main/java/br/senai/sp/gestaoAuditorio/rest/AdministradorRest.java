package br.senai.sp.gestaoAuditorio.rest;

import java.net.URI;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import br.senai.sp.gestaoAuditorio.annotation.Publico;
import br.senai.sp.gestaoAuditorio.model.Administrador;
import br.senai.sp.gestaoAuditorio.model.TokenJWT;
import br.senai.sp.gestaoAuditorio.repository.AdministradorRepository;

@RestController
@RequestMapping("/api/administrador")
public class AdministradorRest {

	@Autowired
	private AdministradorRepository repository;
	// constantes para gerar o token
	public static final String EMISSOR = "r@meu";
	public static final String SECRET = "RES@PLICA";

	@Publico
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Administrador> findAdmin(@PathVariable("id") Long idAdmin) {
		// busca o adm
		Optional<Administrador> admin = repository.findById(idAdmin);
		if (admin.isPresent()) {
			return ResponseEntity.ok(admin.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@Publico
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> atualizarAdm(@RequestBody Administrador administrador, @PathVariable("id") Long id) {
		if (id != administrador.getId()) {
			throw new RuntimeException("id invalido");
		}
		System.out.println("OIIII");
		// salvar o usuario no banco de dados
		repository.save(administrador);
		// criar um cabeçalhao HTTp
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(URI.create("/api/administrador/"));
		return new ResponseEntity<Void>(headers, HttpStatus.OK);
	}
	
	@Publico
	@RequestMapping(value = "", method = RequestMethod.GET)
	public Iterable<Administrador> getAllValues() {
		return repository.findAll();
	}
	@Publico
	@RequestMapping(value = "/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TokenJWT> logar(@RequestBody Administrador administrador) {
		System.out.println(administrador.getNif());
		System.out.println(administrador.getSenha());
		// busca o usuário no BD
		administrador = repository.findByNifAndSenha(administrador.getNif(), administrador.getSenha());
		// verifica se existe o usuário
		if (administrador != null) {
			System.out.println("Entrou aqui !");
			// valores adicionais para o token
			Map<String, Object> payload2 = new HashMap<String, Object>();
			payload2.put("id_admin", administrador.getId());
			payload2.put("nome_admin", administrador.getNome());
			payload2.put("tipoAdm", "administrador");
			// definir a data de expiração
			Calendar expiracao = Calendar.getInstance();
			expiracao.add(Calendar.HOUR, 1);
			// algoritmo para assinar o token
			Algorithm algoritmo2 = Algorithm.HMAC256(SECRET);
			// gerar o token
			TokenJWT tokenJwt2 = new TokenJWT();
			tokenJwt2.setToken(JWT.create().withPayload(payload2).withIssuer(EMISSOR).withExpiresAt(expiracao.getTime())
					.sign(algoritmo2));
			
			System.out.println(tokenJwt2);
			return ResponseEntity.ok(tokenJwt2);
			
		} else {
			return new ResponseEntity<TokenJWT>(HttpStatus.UNAUTHORIZED);
		}
	}
}


