package br.senai.sp.gestaoAuditorio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;


import br.senai.sp.gestaoAuditorio.model.Evento;

public interface EventoRepository extends PagingAndSortingRepository<Evento, Long> {
	
	public Evento findByStartAndEnd(String start, String end);
	
	@Query("SELECT t FROM Evento t where t.start  BETWEEN :s AND :e")
	public Evento intervaloDeDatas(@Param("s") String start, @Param("e") String end);

}
