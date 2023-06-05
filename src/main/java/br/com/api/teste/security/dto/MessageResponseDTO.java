package br.com.api.teste.security.dto;

//transporta os dados do Resposta e mensagens da aplicação
public class MessageResponseDTO {
	//definição de atributo
	private String message;
	
	//constutor parametrizado
	public MessageResponseDTO(String message) {
		this.message = message;
	}

	//Get e Set
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}