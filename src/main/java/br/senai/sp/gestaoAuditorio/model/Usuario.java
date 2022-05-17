package br.senai.sp.gestaoAuditorio.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import br.senai.sp.gestaoAuditorio.util.HashUtil;
import lombok.Data;

@Data
@Entity
public class Usuario {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nome;
	@Column(unique = true)
	private String nif;
	
	private String senha;
	
	// metodo que seta a senha sem o hash
			public void setSenhaComHash(String hash) {

				// seta o hash na senha
				this.senha = hash;

			}
			
			// metodo para setar a senha aplicando o hash
			public void setSenha(String senha) {

				// aplica o hash e seta a senha no objeto
				this.senha = HashUtil.hash256(senha);
			}

	
}
