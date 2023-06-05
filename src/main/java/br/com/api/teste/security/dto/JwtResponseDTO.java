package br.com.api.teste.security.dto;

import java.util.List;

//responsável por trazer a resposta da autenticação do JWT - signin
public class JwtResponseDTO {
	
	//definição de atributos
	private String token;
	//Bearer - retorna o tipo do token da requisição 
	private String type = "Bearer";
	private Integer id;
	private String username;
	private String email;
	private List<String> roles;

	//construtor parametrizado
	public JwtResponseDTO(String accessToken, Integer id, String username, String email, List<String> roles) {
		this.token = accessToken;
		this.id = id;
		this.username = username;
		this.email = email;
		this.roles = roles;
	}

	//Getters e Setters
	public String getAccessToken() {
		return token;
	}

	public void setAccessToken(String accessToken) {
		this.token = accessToken;
	}

	public String getTokenType() {
		return type;
	}

	public void setTokenType(String tokenType) {
		this.type = tokenType;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<String> getRoles() {
		return roles;
	}
}