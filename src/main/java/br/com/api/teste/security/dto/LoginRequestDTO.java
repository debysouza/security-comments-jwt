package br.com.api.teste.security.dto;

import jakarta.validation.constraints.NotBlank;

//utilizada para receber uma solicitação de login de um cliente
//o cliente envia o nome de usuário e senha como objeto json que é mapeado por essa classe
public class LoginRequestDTO {
	
	//definição de atributos
	@NotBlank
	private String username;

	@NotBlank
	private String password;

	//Getters e Setters
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}