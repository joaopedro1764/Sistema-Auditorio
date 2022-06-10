package br.senai.sp.gestaoAuditorio.rest;

import java.net.URI;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
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

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import br.senai.sp.gestaoAuditorio.annotation.Privado;
import br.senai.sp.gestaoAuditorio.annotation.Publico;
import br.senai.sp.gestaoAuditorio.model.Erro;
import br.senai.sp.gestaoAuditorio.model.TokenJWT;
import br.senai.sp.gestaoAuditorio.model.Usuario;
import br.senai.sp.gestaoAuditorio.repository.UsuarioRepository;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioRest {
	@Autowired
	private UsuarioRepository repository;
	// constantes para gerar o token
	public static final String EMISSOR = "r@meu";
	public static final String SECRET = "RES@PLICA";

	@Privado
	@RequestMapping(value = "", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> criarUsuario(@RequestBody Usuario usuario) {
		try {
			// savar o usuario no banco de dados
			repository.save(usuario);
			// retronar codigo 201 , com url para acesso no location eo usuario
			// no corpo da resposta
			return ResponseEntity.created(URI.create("/" + usuario.getId())).body(usuario);
		} catch (DataIntegrityViolationException e) {
			e.printStackTrace();
			Erro erro = new Erro();
			erro.setStatusCode(500);
			erro.setMensagem("Erro de Contraint: registro Duplicado");
			erro.setExcepiton(e.getClass().getName());
			return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			e.printStackTrace();
			Erro erro = new Erro();
			erro.setStatusCode(500);
			erro.setMensagem("Erro ");
			erro.setExcepiton(e.getClass().getName());
			return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Privado
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Usuario> findUsuario(@PathVariable("id") Long idUsuario) {
		Optional<Usuario> usuario = repository.findById(idUsuario);
		if (usuario.isPresent()) {
			return ResponseEntity.ok(usuario.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@Privado
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> ExcluirUsuario(@PathVariable("id") Long idUsuario) {
		repository.deleteById(idUsuario);
		return ResponseEntity.noContent().build();
	}

	@Privado
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> atualizarAdm(@RequestBody Usuario usuario, @PathVariable("id") Long id) {
		if (id != usuario.getId()) {
			throw new RuntimeException("id invalido");
		}
		// salvar o usuario no banco de dados
		repository.save(usuario);
		// criar um cabeçalhao HTTp
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(URI.create("/api/usuario/"));
		return new ResponseEntity<Void>(headers, HttpStatus.OK);
	}

	@Publico
	@RequestMapping(value = "", method = RequestMethod.GET)
	public Iterable<Usuario> getAllValues() {
		return repository.findAll();
	}

	@Publico
	@RequestMapping(value = "/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TokenJWT> logar(@RequestBody Usuario usuario) {
		System.out.println(usuario.getNif());
		System.out.println(usuario.getSenha());
		// busca o usuário no BD
		usuario = repository.findByNifAndSenha(usuario.getNif(), usuario.getSenha());
		// verifica se existe o usuário
		if (usuario != null) {
			System.out.println("Entrou aqui !");
			// valores adicionais para o token
			Map<String, Object> payload = new HashMap<String, Object>();
			payload.put("id_usuario", usuario.getId());
			payload.put("nome_usuario", usuario.getNome());
			payload.put("tipo", usuario.getTipo());
			// definir a data de expiração
			Calendar expiracao = Calendar.getInstance();
			expiracao.add(Calendar.HOUR, 1);
			// algoritmo para assinar o token
			Algorithm algoritmo = Algorithm.HMAC256(SECRET);
			// gerar o token
			TokenJWT tokenJwt = new TokenJWT();
			tokenJwt.setToken(JWT.create().withPayload(payload).withIssuer(EMISSOR).withExpiresAt(expiracao.getTime())
					.sign(algoritmo));

			System.out.println(tokenJwt);
			return ResponseEntity.ok(tokenJwt);

		} else {
			return new ResponseEntity<TokenJWT>(HttpStatus.UNAUTHORIZED);
		}
	}
}
