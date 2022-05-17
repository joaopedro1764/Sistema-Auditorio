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
import br.senai.sp.gestaoAuditorio.model.Administrador;
import br.senai.sp.gestaoAuditorio.repository.AdministradorRepository;
import br.senai.sp.gestaoAuditorio.util.HashUtil;

@Controller
public class AdministradorController {

	@Autowired
	AdministradorRepository repository;

	@RequestMapping("formAdmin")
	public String form() {

		return "Administrador/Form";
	}

	@Privado
	@RequestMapping("salvarAdmin")
	public String salvarAdministrador(Administrador administrador) {

		boolean alteracao = administrador.getId() != null ? true : false;
		System.out.println("pasou 1");
		// verifica se a senha é igual a hash null
		if (administrador.getSenha().equals(HashUtil.hash256(""))) {

			if (!alteracao) {

				// pega a "senha" que sera da primeira ate a ultima letra do email .
				// substring pega o inicio.
				// indexof pega ate onde que da string, na senha vai ate 0 @.
				String parte = administrador.getNif().substring(0, 3);
				System.out.println(parte);
				// seta a primeira parte do nif que sera a nova senha.
				administrador.setSenha(parte);
			} else {

				// busca a senha atual pelo id do adm
				String senha = repository.findById(administrador.getId()).get().getSenha();

				// seta a senha com hash
				administrador.setSenha(senha);
			}
		}

		try {
			repository.save(administrador);

			return "redirect:formAdmin";

		} catch (Exception e) {

			// caso ocorra um erro informa ao usuario de forma melhor

		}
		return "redirect:formAdmin";
	}

	@Privado
	@RequestMapping("listarAdmin/{pagina}")

	// @PathVariable associando int page a ${page}
	public String listarAdmin(Model model, @PathVariable("pagina") int page) {

		// cria uma pagina que começa na 0, que possuem 6 elementos por paginas e ordena
		// pelo nome
		PageRequest pageble = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.ASC, "nome"));

		// cria a pagina atual atraves do repository

		Page<Administrador> pagina = repository.findAll(pageble);

		// descobrir o total de pagina
		int totalPg = pagina.getTotalPages();

		// cria uma lista de inteiros para representar as paginas
		List<Integer> pageNumbers = new ArrayList<Integer>();

		for (int i = 0; i < totalPg; i++) {

			pageNumbers.add(i + 1);
		}
		// adiciona as variaveis na model

		// pendurando os admins cadastrados
		model.addAttribute("admin", pagina.getContent());
		// pagina atual
		model.addAttribute("pgAtual", page);
		//
		model.addAttribute("numTotalPg", totalPg);
		// quantidade de paginas com base nos cadastros
		model.addAttribute("numPg", pageNumbers);
		// retorna para o html da lista
		return "Administrador/Lista";
	}

	@Privado
	@RequestMapping("alterarAdmin")
	public String alterarAdmin(Model model, Long id) {

		model.addAttribute("admin", repository.findById(id).get());

		return "forward:formAdmin";

	}

	@RequestMapping("excluirAdmin")
	public String excluirAdmin(Long id) {

		repository.deleteById(id);

		return "redirect:listarAdmin/1";

	}

	@Publico
	@RequestMapping("loginAdmin")
	public String login(Administrador administrador, RedirectAttributes attr, HttpSession session) {

		Administrador adm = repository.findByNifAndSenha(administrador.getNif(), administrador.getSenha());

		if (adm == null) {

			attr.addFlashAttribute("mensagemErro", "Login Invalido");

			return "redirect:/";
		} else {

			session.setAttribute("adminLogado", adm);
			return "redirect:listarAdmin/1";

		}

	}

	@RequestMapping("logout")
	public String logout(HttpSession session) {
		// elimina o usuário da session
		session.invalidate();
		// retorna para a barra inicial
		return "redirect:/";
	}

}
