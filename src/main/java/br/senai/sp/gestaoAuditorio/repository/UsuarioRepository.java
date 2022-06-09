package br.senai.sp.gestaoAuditorio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import br.senai.sp.gestaoAuditorio.model.Usuario;

public interface UsuarioRepository extends PagingAndSortingRepository<Usuario, Long> {

	public Usuario findByNifAndSenha(String nif, String senha);

	public Usuario findByNif(String nif);

	@Query("SELECT t FROM Usuario t WHERE t.nome LIKE %:p% OR t.nif LIKE %:p% OR t.tipo LIKE %:p% ORDER BY t.nome ASC")
	public List<Usuario> buscarUsuario(@Param("p") String parametro);

	public List<Usuario> findAllByOrderByNomeAsc();

}
