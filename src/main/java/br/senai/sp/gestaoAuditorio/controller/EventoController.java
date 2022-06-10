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

import br.senai.sp.gestaoAuditorio.annotation.Publico;
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

	@Publico
	@RequestMapping("painelReserva")
	public String painelReserva(Model model) {

		model.addAttribute("evento", repositoryEvento.findAll());

		return "Interface/PainelReserva";
	}

	@RequestMapping("historico")
	public String historico(Model model) {
		return "Interface/historico";
	}

	@RequestMapping("alterarFoto")
	public String alterarFoto(Model model, Long idFoto) {
		Evento evento = repositoryEvento.findById(idFoto).get();
		model.addAttribute("historico", evento);
		return "forward:historico";
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

		return "redirect:listarHistorico/1";
	}

	@RequestMapping("listarHistorico/{page}")
	public String listar(Model model, @PathVariable("page") int page) {
		// caso queira ordenar por algum campo, acrescenta-se o Sort.by no 3º parâmetro
		PageRequest pageable = PageRequest.of(page - 1, 6, Sort.by(Sort.Direction.ASC, "id"));
		Page<Evento> pagina = repositoryEvento.findAll(pageable);
		int totalPages = pagina.getTotalPages();
		model.addAttribute("historico", pagina.getContent());
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("currentPage", page);

		List<Integer> pageNumbers = new ArrayList<Integer>();
		for (int i = 0; i < totalPages; i++) {
			pageNumbers.add(i + 1);
		}
		model.addAttribute("pageNumbers", pageNumbers);
		return "Interface/historicoLista";
	}

	@RequestMapping("excluir")
	public String excluirRestaurante(Long idFotos) {
		Evento eve = repositoryEvento.findById(idFotos).get();
		if (eve.getFotos().length() > 0) {
			for (String foto : eve.verFotos()) {
				fireBaseUtil.deletar(foto);
			}
		}
		repositoryEvento.delete(eve);
		return "redirect:listarHistorico/1";
	}

	@RequestMapping("excluirFoto")
	public String excluirFoto(Long idFotos, int numFoto, Model model) {
		Evento eve = repositoryEvento.findById(idFotos).get();
		String fotoUrl = eve.verFotos()[numFoto];
		fireBaseUtil.deletar(fotoUrl);
		eve.setFotos(eve.getFotos().replace(fotoUrl + ";", ""));
		repositoryEvento.save(eve);
		model.addAttribute("historico", eve);
		return "forward:/listarHistorico/1";
	}

	// request mapping para buscar
	@RequestMapping("buscarHistorico")
	public String buscar(Model model, String parametro) {
		// busca
		List<Evento> title = repositoryEvento.findByTitleAndStart(parametro);
		if (title.size() == 0) {
			model.addAttribute("mensagemErro", "Nenhuma correspondência encontrada");
		} else {
			model.addAttribute("historico", repositoryEvento.findByTitleAndStart(parametro));
		}
		return "Interface/historicoLista";
	}

}
