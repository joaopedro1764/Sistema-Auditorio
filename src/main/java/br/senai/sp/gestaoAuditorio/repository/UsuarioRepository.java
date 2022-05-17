package br.senai.sp.gestaoAuditorio.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import br.senai.sp.gestaoAuditorio.model.Usuario;

public interface UsuarioRepository extends PagingAndSortingRepository<Usuario, Long> {

	public Usuario findByNifAndSenha(String nif, String senha);

}
