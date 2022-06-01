package br.senai.sp.gestaoAuditorio.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import br.senai.sp.gestaoAuditorio.model.Evento;

public interface EventoRepository extends PagingAndSortingRepository<Evento, Long> {

	@Query("SELECT t FROM Evento t where (t.start BETWEEN :s AND :e) OR (t.end BETWEEN :s AND :e)")
	public Evento intervaloForaHoras(@Param("s") LocalDateTime start, @Param("e") LocalDateTime end);
	
	@Query("SELECT t FROM Evento t where (:s BETWEEN t.start AND t.end) OR (:e BETWEEN t.start AND t.end)")
	public Evento intervaloDeDatas(@Param("s") LocalDateTime start, @Param("e") LocalDateTime end);
	
<<<<<<< HEAD
	
	@Query("SELECT t FROM Evento t where (:s BETWEEN t.start AND t.end) OR (:e BETWEEN t.start AND t.end)")
	public List<Evento> intervaloDeDatasComLista(@Param("s") LocalDateTime start, @Param("e") LocalDateTime end);
	
	
	@Query("SELECT t FROM Evento t where (t.start BETWEEN :s AND :e) OR (t.end BETWEEN :s AND :e)")
	public List<Evento> intervaloForaHorasComLista(@Param("s") LocalDateTime start, @Param("e") LocalDateTime end);
	
	
	
	
	
	public Evento findByStart(LocalDateTime start);
	
	public Evento findByEnd( LocalDateTime end);
	
	public Evento findByStartAndEnd(LocalDateTime start, LocalDateTime end);
	
	public Evento findByTitle(String title);

=======
	public List<Evento> findByUsuarioId(Long id);
	
>>>>>>> 0dc5dbf975d98dfad3ba7586269eb4711fd2869c
}




