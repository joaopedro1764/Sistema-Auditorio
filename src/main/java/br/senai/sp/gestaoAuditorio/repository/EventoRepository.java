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

	@Query("SELECT t FROM Evento t where (:s BETWEEN t.start AND t.end) OR (:e BETWEEN t.start AND t.end)")
	public List<Evento> intervaloDeDatasComLista(@Param("s") LocalDateTime start, @Param("e") LocalDateTime end);

	@Query("SELECT t FROM Evento t where (t.start BETWEEN :s AND :e) OR (t.end BETWEEN :s AND :e)")
	public List<Evento> intervaloForaHorasComLista(@Param("s") LocalDateTime start, @Param("e") LocalDateTime end);

	public Evento findByIdAndFotos(Long idEvento, String fotos);

	public Evento findByStart(LocalDateTime start);

	public Evento findByEnd(LocalDateTime end);

	public Evento findByStartAndEnd(LocalDateTime start, LocalDateTime end);

	public Evento findByTitle(String title);

	public List<Evento> findByUsuarioId(Long id);

	@Query("SELECT t FROM Evento t WHERE t.title LIKE %:p% OR t.start LIKE %:p% ORDER BY t.title ASC")
	public List<Evento> findByTitleAndStart(@Param("p") String parametro);

	public List<Evento> findAllByOrderByTitleAsc();

	@Query("SELECT t FROM Evento t where :s BETWEEN t.start AND t.end")
	public Evento intervaloDeDatasInicio(@Param("s") LocalDateTime start);

	@Query("SELECT t FROM Evento t where :e BETWEEN t.start AND t.end")
	public Evento intervaloDeDatasFinal(@Param("e") LocalDateTime end);

}
