package br.senai.sp.gestaoAuditorio.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import br.senai.sp.gestaoAuditorio.repository.EventoRepository;
import br.senai.sp.gestaoAuditorio.rest.EventoRest;

@Controller
public class EventoController {

	@Autowired
	private EventoRepository repositoryEvento;

	@RequestMapping("fullCalendar")
	public String form() {
		return "Interface/Eventos";
	}

	@RequestMapping("verPorId")
	public String verPeloid(Long id, Model model) {

		// model.addAttribute("info", repository.findById(id).get());

		return "forward:fullCalendar";
	}

<<<<<<< HEAD
	@RequestMapping("dataInvalida")
	public String data(Evento evento, RedirectAttributes attr) {

		EventoRest rest = new EventoRest();
		
		rest.criarEvento(evento, attr);
		attr.addFlashAttribute("mensagemErro", "DATA JA RESERVADA");
		
		return "redirect:fullCalendar";
	}
	
	
	
		
=======
	@RequestMapping("painelReserva")
	public String painelReserva(Model model) {

		model.addAttribute("evento", repositoryEvento.findAll());

		return "Interface/PainelReserva";
	}
>>>>>>> 0dc5dbf975d98dfad3ba7586269eb4711fd2869c

}
