package br.senai.sp.gestaoAuditorio.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
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
	@RequestMapping("salvaFotos")
	public String salvaFotos(Evento evento, @RequestParam("fileFotos") MultipartFile[] fileFotos) {
		// String da url das fotos
		String fotos = evento.getFotos();
		for(MultipartFile arquivo : fileFotos) {
			if(arquivo.getOriginalFilename().isEmpty()) {
				continue;
			}
			try {
				fotos += fireBaseUtil.uploadFile(arquivo) + ";";
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
		evento.setFotos(fotos);
		repositoryEvento.save(evento);
		return "redirect:painelReserva";
	}
	@RequestMapping("listaFoto/{page}")
	public String listaFotos(Model model, @PathVariable("page") int page) {
		PageRequest pageble = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.ASC, "id"));
		Page<Evento> pagina = repositoryEvento.findAll(pageble);
		int totalPages = pagina.getTotalPages();
		model.addAttribute("listaFoto", pagina.getContent());
		model.addAttribute("totalPaginas", totalPages);
		model.addAttribute("paginaAtual", page);
		List<Integer> pageNumbers = new ArrayList<Integer>();
		for (int i = 0; i < totalPages; i++) {
			pageNumbers.add(i + 1);
		}
		model.addAttribute("numPaginas", pageNumbers);
		return "Interface/FotosLista";
	}
	@RequestMapping("excluir")
	public String excluir(Long idFotos) {
		Evento eve = repositoryEvento.findById(idFotos).get();
		if(eve.getFotos().length() > 0) {
			for(String foto : eve.verFotos()) {
				fireBaseUtil.deletar(foto);
			}
		}
		repositoryEvento.delete(eve);
		return "redirect:listaFoto/1";
	}
	
	@RequestMapping("excluirFoto")
	public String excluirFoto(Long idFoto, int numFoto, Model model) {
		Evento eve = repositoryEvento.findById(idFoto).get();
		String fotoUrl = eve.verFotos()[numFoto];
		fireBaseUtil.deletar(fotoUrl);
		eve.setFotos(eve.getFotos().replace(fotoUrl + ";", ""));
		repositoryEvento.save(eve);
		model.addAttribute("listaFoto", eve);
		return "forward:painelReserva";
	}
	@RequestMapping("alterarFoto")
	public String alterarFoto(Model model, Long idFoto) {
		Evento evento = repositoryEvento.findById(idFoto).get();
		model.addAttribute("listaFoto", evento);
		return "forward:painelReserva";
	}

}
