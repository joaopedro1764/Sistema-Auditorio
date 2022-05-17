package br.senai.sp.gestaoAuditorio.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import br.senai.sp.gestaoAuditorio.model.Evento;

public interface EventoRepository extends PagingAndSortingRepository<Evento, Long> {

}
