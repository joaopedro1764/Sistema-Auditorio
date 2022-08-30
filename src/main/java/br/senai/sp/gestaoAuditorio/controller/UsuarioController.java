package br.senai.sp.gestaoAuditorio.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.senai.sp.gestaoAuditorio.annotation.Privado;
import br.senai.sp.gestaoAuditorio.annotation.Publico;
import br.senai.sp.gestaoAuditorio.model.Usuario;
import br.senai.sp.gestaoAuditorio.repository.EventoRepository;
import br.senai.sp.gestaoAuditorio.repository.UsuarioRepository;
import br.senai.sp.gestaoAuditorio.util.HashUtil;

@Controller
public class UsuarioController {

	@Autowired
	private UsuarioRepository repository;

	@Autowired
	private EventoRepository repositoryEvento;

	@RequestMapping("formUsuario")
	public String formUsuario() {
		return "Usuario/Form";
	}
	@RequestMapping("manual")
	public String Manual() {
		return "interface/Manual";
	}

	@RequestMapping("buscarUsuario")
	public String buscar(Model model, String parametro) {
		// busca
		List<Usuario> usuario = repository.buscarUsuario(parametro);
		if (usuario.size() == 0) {
			model.addAttribute("mensagemErro", "Nenhuma correspondência encontrada");
		} else {
			model.addAttribute("usuario", repository.buscarUsuario(parametro));
		}
		return "Usuario/Lista";
	}

	@RequestMapping("salvarUsuario")
	public String salvarUsuario(Usuario usuario) {

		boolean alteracao = usuario.getId() != null ? true : false;

		// verifica se a senha é igual a hash null
		if (usuario.getSenha().equals(HashUtil.hash256(""))) {

			if (!alteracao) {

				// pega a "senha" que sera da primeira ate a ultima letra do email .
				// substring pega o inicio.
				// indexof pega ate onde que da string, na senha vai ate 0 @.
				String parte = usuario.getNif().substring(0, 3);

				// seta a primeira parte do email que sera a nova senha.
				usuario.setSenha(parte);
			} else {

				// busca a senha atual pelo id do usuario
				String senha = repository.findById(usuario.getId()).get().getSenha();

				// seta a senha com hash
				usuario.setSenha(senha);
			}
		}

		try {
			repository.save(usuario);

			return "redirect:listarUsuario/1";

		} catch (Exception e) {

			// caso ocorra um erro informa ao usuario de forma melhor

		}
		return "redirect:listarUsuario/1";
	}

	@Publico
	@RequestMapping("listarUsuario/{pagina}")

	// @PathVariable associando int page a ${page}
	public String listarUsuario(Model model, @PathVariable("pagina") int page) {

		// cria uma pagina que começa na 0, que possuem 6 elementos por paginas e ordena
		// pelo nome
		PageRequest pageble = PageRequest.of(page - 1, 100, Sort.by(Sort.Direction.ASC, "nome"));

		// cria a pagina atual atraves do repository

		Page<Usuario> pagina = repository.findAll(pageble);

		// descobrir o total de pagina
		int totalPg = pagina.getTotalPages();

		// cria uma lista de inteiros para representar as paginas
		List<Integer> pageNumbers = new ArrayList<Integer>();

		for (int i = 0; i < totalPg; i++) {

			pageNumbers.add(i + 1);
		}
		// adiciona as variaveis na model

		// pendurando os usuario cadastrados
		model.addAttribute("usuario", pagina.getContent());
		// pagina atual
		model.addAttribute("pgAtual", page);
		//
		model.addAttribute("numTotalPg", totalPg);
		// quantidade de paginas com base nos cadastros
		model.addAttribute("numPg", pageNumbers);
		// retorna para o html da lista
		return "Usuario/Lista";
	}

	@RequestMapping("alterarUsuario")
	public String alterarUsuario(Model model, Long id) {

		model.addAttribute("user", repository.findById(id).get());

		return "forward:listarUsuario/1";

	}

	@Privado
	@RequestMapping("logOut")
	public String logOut(HttpSession httpSession) {

		// elimina o usuario da sessao
		httpSession.invalidate();

		return "redirect:/";
	}

	@RequestMapping("excluirUsuario")
	public String excluirUsuario(Long id) {

		repository.deleteById(id);

		return "redirect:listarUsuario/1";

	}

	@RequestMapping("interfaceUsuario")
	public String painelUsuario() {

		return "Interface/PainelUsuario";
	}

	@RequestMapping("perfilUsuario")
	public String perfilUsuario() {

		return "Interface/Perfil";
	}

	@RequestMapping("alterarUser")
	public String alterarUser(Model model, Long id) {

		model.addAttribute("u", repository.findById(id).get());

		return "forward:listarUsuario/1";

	}

	@RequestMapping("loginUsuario")
	public String login(Usuario usuario, Model model, Long id, RedirectAttributes attr, HttpSession session) {

		Usuario user = repository.findByNifAndSenha(usuario.getNif(), usuario.getSenha());

		if (user == null) {

			attr.addFlashAttribute("mensagemErro", "Login Invalido");

			return "redirect:/";
		} else {
			System.out.println(user);
			System.out.println(repositoryEvento.findAll());
			session.setAttribute("usuarioLogado", user);
			return "redirect:painelReserva";

		}

	}

	@RequestMapping("esquecerSenha")
	public String esquecerSenha(Usuario usuario, Model model, RedirectAttributes attr) {

		Usuario usuario2 = repository.findByNif(usuario.getNif());

		System.out.println("primeiro nif " + usuario.getNif());

		if (usuario2 != null) {

			attr.addFlashAttribute("senha", "Sua senha foi redefinida para a senha inicial!");

			String parte = repository.findById(usuario2.getId()).get().getNif().substring(0, 3);

			System.out.println("u1 " + usuario);
			System.out.println("u2 " + usuario2);

			usuario2.setSenha(parte);

			repository.save(usuario2);

			System.out.println("3 primeiros caracteres do nif: " + parte);

			return "redirect:/";

		} else {

			System.out.println("DEU INDEX");

			attr.addFlashAttribute("msg", "Nif não encontrado.");

			return "redirect:/";

		}

	}
}
