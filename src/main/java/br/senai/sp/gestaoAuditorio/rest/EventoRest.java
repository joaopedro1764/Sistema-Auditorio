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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.senai.sp.gestaoAuditorio.annotation.Privado;
import br.senai.sp.gestaoAuditorio.controller.JavaMailApp;
import br.senai.sp.gestaoAuditorio.model.Erro;
import br.senai.sp.gestaoAuditorio.model.Evento;
import br.senai.sp.gestaoAuditorio.repository.EventoRepository;

@RestController
@RequestMapping("/api/evento")
public class EventoRest {
	// constantes para gerar o token
	@Autowired
	private EventoRepository repository;
	public static final String EMISSOR = "gestao@Auditorio";
	public static final String SECRET = "Audit@orio";

	@Privado
	@RequestMapping(value = "", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> criarEvento(@RequestBody Evento evento, RedirectAttributes attr) {
		try {

			Evento evn2 = repository.intervaloDeDatas(evento.getStart(), evento.getEnd());
			if (evn2 == null) {
				System.out.println(evn2);
				System.out.println("PRIMEIRO IF");

				evn2 = repository.intervaloForaHoras(evento.getStart(), evento.getEnd());
				if (evn2 == null) {

					System.out.println("SALVOU");
					repository.save(evento);

				} else {

					System.out.println("ERRO IF INTERNO");
					return ResponseEntity.status(HttpStatus.CONFLICT).build();
				}

			} else {
				System.out.println("ERROOOO");
				attr.addFlashAttribute("mensagemErro", "DATA JA RESERVADA");
				return ResponseEntity.status(HttpStatus.CONFLICT).build();
			}
			// salvar o usuário no banco de dados
			// retorna código 201, com a URL para acesso no Location e o usuário inserido no
			// corpo da resposta

			JavaMailApp.mandarEmail(evento);

			// JavaMailApp.mandarEmail(evento);

			return ResponseEntity.created(URI.create("/api/evento/" + evento.getId())).body(evento);

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

	@Privado
	@RequestMapping(value = "", method = RequestMethod.GET)
	public Iterable<Evento> getAllValues() {
		return repository.findAll();
	}

	// Pelo id
	@Privado
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Evento> findEvento(@PathVariable("id") Long idEvento) {
		// busca o evento
		Optional<Evento> eveto = repository.findById(idEvento);
		if (eveto.isPresent()) {
			return ResponseEntity.ok(eveto.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@Privado
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> atulizarEvento(@RequestBody Evento evento, @PathVariable("id") Long id) {

		if (id != evento.getId()) {

			throw new RuntimeException("id invalido");
		} else {

			Evento evn2 = repository.intervaloDeDatasInicio(evento.getStart());

			Evento evn3 = repository.intervaloDeDatasFinal(evento.getEnd());

			Evento evn4 = repository.findByTitle(evento.getTitle());

			System.out.println("EVN 1: " + evento);
			System.out.println("EVN 2: " + evn2);

			if (evn2 != null && evn3 != null && evn4 != null) {

				System.out.println("ERROOO");

				return ResponseEntity.status(HttpStatus.CONFLICT).build();

			} else {
				repository.save(evento);
			}
			HttpHeaders headers = new HttpHeaders();
			headers.setLocation(URI.create("/api/evento/"));
			return new ResponseEntity<Void>(headers, HttpStatus.OK);

		}
	}

	@Privado
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> ExcluirReserva(@PathVariable("id") Long idReserva) {
		repository.deleteById(idReserva);

		return ResponseEntity.noContent().build();

	}

	@Privado
	@RequestMapping(value = "/usuario/{id}", method = RequestMethod.GET)
	public Iterable<Evento> procurarID(@PathVariable("id") Long id) {
		return repository.findByUsuarioId(id);
	}
}
