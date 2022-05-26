package br.senai.sp.gestaoAuditorio.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.senai.sp.gestaoAuditorio.model.Evento;
import br.senai.sp.gestaoAuditorio.repository.EventoRepository;

@Controller
public class EventoController {
	@Autowired
	EventoRepository repository;

	@RequestMapping("fullCalendar")
	public String form() {
		return "Fullcalendar/Eventos";
	}

	@RequestMapping("salvarEvento")
	public String salvarEvento(Evento evento, String start, String end) {

		System.out.println("Passou no salvar");
		
			
		

		return "redirect:fullCalendar";

	}

	@RequestMapping("verPorId")
	public String verPeloid(Long id, Model model) {

		model.addAttribute("info", repository.findById(id).get());

		return "forward:fullCalendar";
	}

	@RequestMapping("dataInvalida")
	public String data(String start, String end) {

		return "";
	}
	
		

}
