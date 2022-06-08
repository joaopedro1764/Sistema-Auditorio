package br.senai.sp.gestaoAuditorio.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.senai.sp.gestaoAuditorio.model.Evento;
import br.senai.sp.gestaoAuditorio.repository.EventoRepository;
import br.senai.sp.gestaoAuditorio.rest.EventoRest;
import br.senai.sp.gestaoAuditorio.util.FireBaseUtil;

@Controller
public class EventoController {

	@Autowired
	private EventoRepository repositoryEvento;
	@Autowired
	private FireBaseUtil fireBaseUtil;

	@RequestMapping("fullCalendar")
	public String form() {
		return "Interface/Eventos";
	}

	@RequestMapping("verPorId")
	public String verPeloid(Long id, Model model) {

		// model.addAttribute("info", repository.findById(id).get());

		return "forward:fullCalendar";
	}

	@RequestMapping("dataInvalida")
	public String data(Evento evento, RedirectAttributes attr) {

		EventoRest rest = new EventoRest();

		rest.criarEvento(evento, attr);
		attr.addFlashAttribute("mensagemErro", "DATA JA RESERVADA");

		return "redirect:fullCalendar";
	}

	@RequestMapping("painelReserva")
	public String painelReserva(Model model) {

		model.addAttribute("evento", repositoryEvento.findAll());

		return "Interface/PainelReserva";
	}

	@RequestMapping("historico")
	public String historico(Model model) {
		return "Interface/listaHistorico";
	}

	@RequestMapping("alterarFoto")
	public String alterarFoto(Model model, Long idFoto) {
		Evento evento = repositoryEvento.findById(idFoto).get();
		model.addAttribute("listaFoto", evento);
		return "forward:painelReserva";
	}

	@RequestMapping("salvarHistorico")
	public String salvarHistorico(Evento evento, @RequestParam("fileFotos") MultipartFile[] fileFotos,
			@RequestParam("idEvento") Long idEvento) {
		// String para a url das fotos
		String fotos = evento.getFotos() == null ? "" : evento.getFotos();
		for (MultipartFile arquivo : fileFotos) {
			System.out.println("ENTREI NO FOR");
			System.out.println(arquivo);
			if (arquivo.getOriginalFilename().isEmpty()) {
				continue;
			}
			try {
				fotos += fireBaseUtil.uploadFile(arquivo);
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}

		// evento.setFotos(fotos);
		System.out.println("AAAA");
		evento = repositoryEvento.findById(idEvento).get();
		System.out.println(evento);
		evento.setFotos(fotos);
		repositoryEvento.save(evento);

		return "redirect:historico";
	}

}
