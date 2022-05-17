package br.senai.sp.gestaoAuditorio.model;


import lombok.Data;

@Data
public class Erro {

		private int statusCode;
		private String mensagem;
		private String excepiton;

	}

