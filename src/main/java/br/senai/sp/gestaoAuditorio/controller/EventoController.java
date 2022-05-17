package br.senai.sp.gestaoAuditorio.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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
	public String salvarEvento(Evento evento) {

		System.out.println("Passou no salvar");
		repository.save(evento);

		return "redirect:fullCalendar";

	}
}
