package br.senai.sp.gestaoAuditorio.rest;

import java.net.URI;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.senai.sp.gestaoAuditorio.annotation.Publico;
import br.senai.sp.gestaoAuditorio.model.Administrador;
import br.senai.sp.gestaoAuditorio.repository.AdministradorRepository;

@RestController
@RequestMapping("/api/administrador")
public class AdministradorRest {

	@Autowired
	private AdministradorRepository repository;

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
		// salvar o usuario no banco de dados
		repository.save(administrador);
		// criar um cabeçalhao HTTp
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(URI.create("/api/usuario/"));
		return new ResponseEntity<Void>(headers, HttpStatus.OK);

	}

}
