package br.senai.sp.gestaoAuditorio.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.checkerframework.checker.formatter.qual.Format;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Entity
@Data
public class Evento {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String title;
	private String periodo;
	/* @JsonFormat(pattern = "dd-MM-yyyy") */
	private String start;
	private String end;
	
}
