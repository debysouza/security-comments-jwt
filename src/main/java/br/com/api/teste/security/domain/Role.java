package br.com.api.teste.security.domain;

import br.com.api.teste.security.enums.RoleEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

//É uma entidade que representa um papel ou função de um usuário em um sistema.
@Entity //representa uma entidade
@Table(name = "roles") //indica a entidade como tabela no banco
public class Role {
	@Id //identifica o elemento único na tabela
	@GeneratedValue(strategy = GenerationType.IDENTITY) //geração automática do id
	private Integer id; //id é uma variável do tipo integer com modificador de acesso private

	@Enumerated(EnumType.STRING) //informar no banco o armazenamento do enum como String
	@Column(length = 20) //indica uma coluna na tabela do banco - length indica o comprimento do valor da referida coluna
	private RoleEnum name; //name é uma variável do tipo RoleEnum, ou seja, só pode receber valores definidos no enum RoleEnum

	//Construtores
	public Role() {
	}

	public Role(RoleEnum name) {
		this.name = name;
	}

	//Getters e Setters
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public RoleEnum getName() {
		return name;
	}

	public void setName(RoleEnum name) {
		this.name = name;
	}
}