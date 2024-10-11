package br.com.api.teste.security.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

//@CrossOrigin-> para permitir que um aplicativo web acesse recursos de outro domínio.
//A propriedade origins = "*", permite acesso de qualquer domínio aos recursos disponíveis.
//A propriedade maxAge indica que o tempo de cache para as respostas de uma solicitação de origem cruzada.
@CrossOrigin(origins = "*", maxAge = 3600)
//@RestController-> indica que a Classe é um Controller Http
@RestController
//@RequestMapping-> é utilizada para mapear uma URL específica para um método
@RequestMapping("/test")
public class TestController {
	
	@GetMapping("/all")
	public String allAccess() {
		return "Public Content.";
	}

	@SecurityRequirement(name="Bearer Auth")
	@PreAuthorize("hasRole('USER')")
	@GetMapping("/user")
	public String userAccess() {
		return "User Content.";
	}

	@SecurityRequirement(name="Bearer Auth")
	@PreAuthorize("hasRole('MOD')")
	@GetMapping("/mod")
	public String moderatorAccess() {
		return "Moderator Board.";
	}

	//@SecurityRequirement-> é usada para especificar os requisitos de segurança necessários para acessar
	//A propriedade name da anotação abaixo se refere ao name da anotação @SecurityScheme que está definida na classe SwaggerConfig
	@SecurityRequirement(name="Bearer Auth")
	//@PreAuthorize-> é utilizada para definir regras de autorização
	//hasAnyRole é uma condição de autorização utilizada na anotação "@PreAuthorize", dando acesso as Roles (USER/ADMIN/MODERATOR)
    @PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/admin")
	public String adminAccess() {
		return "Admin Board.";
	}
}