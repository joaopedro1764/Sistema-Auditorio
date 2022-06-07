package br.senai.sp.gestaoAuditorio.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
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

}
