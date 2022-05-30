package br.senai.sp.gestaoAuditorio.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class EventoController {

	@RequestMapping("fullCalendar")
	public String form() {
		return "Interface/Eventos";
	}

	@RequestMapping("verPorId")
	public String verPeloid(Long id, Model model) {

		// model.addAttribute("info", repository.findById(id).get());

		return "forward:fullCalendar";
	}

	@RequestMapping("painelReserva")
	public String painelReserva(Model model, Long id, HttpSession session) {

		return "Interface/PainelReserva";
	}

}
