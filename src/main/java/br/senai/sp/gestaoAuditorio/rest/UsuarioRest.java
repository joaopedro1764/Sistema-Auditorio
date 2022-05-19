package br.senai.sp.gestaoAuditorio.rest;

import java.net.URI;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.senai.sp.gestaoAuditorio.annotation.Privado;
import br.senai.sp.gestaoAuditorio.annotation.Publico;
import br.senai.sp.gestaoAuditorio.model.Erro;
import br.senai.sp.gestaoAuditorio.model.Evento;
import br.senai.sp.gestaoAuditorio.model.Usuario;
import br.senai.sp.gestaoAuditorio.repository.UsuarioRepository;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioRest {
	@Autowired
	private UsuarioRepository repository;
	// constantes para gerar o token
	public static final String EMISSOR = "gestao@Auditorio";
	public static final String SECRET = "Audit@orio";

	@Publico
	@RequestMapping(value = "", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> criarUsuario(@RequestBody Usuario usuario) {
		try {
			// salvar o usuário no banco de dados
			repository.save(usuario);
			// retorna código 201, com a URL para acesso no Location e o usuário inserido
			// no corpo da resposta
			return ResponseEntity.created(URI.create("/api/usuario/" + usuario.getId())).body(usuario);
		} catch (DataIntegrityViolationException e) {
			e.printStackTrace();
			Erro erro = new Erro();
			erro.setStatusCode(500);
			erro.setMensagem("Erro de Constraint: Registro Duplicado");
			erro.setExcepiton(e.getClass().getName());
			return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			Erro erro = new Erro();
			erro.setStatusCode(500);
			erro.setMensagem("Erro: " + e.getMessage());
			erro.setExcepiton(e.getClass().getName());
			return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@Publico
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Usuario> findUsuario(@PathVariable("id") Long idUsuario) {
		// busca o usuário
		Optional<Usuario> usuario = repository.findById(idUsuario);
		if (usuario.isPresent()) {
			return ResponseEntity.ok(usuario.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@Publico
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> atulizarUsuario(@RequestBody Usuario usuario, @PathVariable("id") Long id) {
		if (id != usuario.getId()) {
			throw new RuntimeException("id invalido");
		}
		// savar o usuario no banco de dados
		repository.save(usuario);
		// criar um cabeçalhao HTTp
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(URI.create("/api/usuario/"));
		return new ResponseEntity<Void>(headers, HttpStatus.OK);

	}

}
