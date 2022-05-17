package br.senai.sp.gestaoAuditorio.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import br.senai.sp.gestaoAuditorio.model.Administrador;

public interface AdministradorRepository extends PagingAndSortingRepository<Administrador, Long> {

	public Administrador findByNifAndSenha(String nif, String senha);

}
