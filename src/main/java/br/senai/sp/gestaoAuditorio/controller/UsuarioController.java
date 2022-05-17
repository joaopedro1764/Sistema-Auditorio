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

import br.senai.sp.gestaoAuditorio.model.Administrador;
import br.senai.sp.gestaoAuditorio.model.Usuario;
import br.senai.sp.gestaoAuditorio.repository.UsuarioRepository;
import br.senai.sp.gestaoAuditorio.util.HashUtil;

@Controller
public class UsuarioController {

	@Autowired
	private UsuarioRepository repository;

	@RequestMapping("formUsuario")
	public String formUsuario() {
		return "Usuario/Form";
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

	@RequestMapping("listarUsuario/{pagina}")

	// @PathVariable associando int page a ${page}
	public String listarUsuario(Model model, @PathVariable("pagina") int page) {

		// cria uma pagina que começa na 0, que possuem 6 elementos por paginas e ordena
		// pelo nome
		PageRequest pageble = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.ASC, "nome"));

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

		model.addAttribute("usuario", repository.findById(id).get());

		return "forward:formUsuario";

	}

	@RequestMapping("excluirUsuario")
	public String excluirUsuario(Long id) {

		repository.deleteById(id);

		return "redirect:listarUsuario/1";

	}

	@RequestMapping("loginUsuario")
	public String login(Usuario usuario, RedirectAttributes attr, HttpSession session) {

		Usuario user = repository.findByNifAndSenha(usuario.getNif(), usuario.getSenha());

		if (user == null) {

			attr.addFlashAttribute("mensagemErro", "Login Invalido");

			return "redirect:/";
		} else {

			session.setAttribute("usuarioLogado", user);
			return "redirect:listarUsuario/1";

		}

	}
}
