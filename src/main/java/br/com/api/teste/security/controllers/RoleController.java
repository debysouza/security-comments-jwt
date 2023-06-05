package br.com.api.teste.security.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.api.teste.security.domain.Role;
import br.com.api.teste.security.services.RoleService;

//@CrossOrigin-> permite a comunicação entre domínios cruzados para o controlador
//propriedades origins-> indica que qualquer origem ou domínio podem fazer solicitações para o controlador (API)
//propriedades maxAge-> especifica o tempo máximo em segundos que a resposta da solicitação pode ser armazenada em cache
@CrossOrigin(origins = "*", maxAge = 3600)
//inidica que a classe é um controlador e responsáveis pelas requisições HTTP
@RestController
//mapeia as solicitações HTTP
@RequestMapping("/roles")
public class RoleController {
	@Autowired
	RoleService roleService;
	
	@PostMapping
	//O método save mapeia a requisição HTTP POST
	public ResponseEntity<Role> save(@RequestBody Role role) {
		//cria o objeto e salva a Role no banco de dados usando o RoleService
		Role newRole = roleService.save(role);
//		Se a operação for bem-sucedida, retorna um ResponseEntity com o código 201 
//		e o objeto Role salvo.
		if(newRole != null)
			return new ResponseEntity<>(newRole, HttpStatus.CREATED);
//		Caso contrário, retorna um ResponseEntity com o código 400 e o objeto 
//		nulo indicando falha na operação.
		else
			return new ResponseEntity<>(newRole, HttpStatus.BAD_REQUEST);
	}

}

//Resumindo, o método save trata a requisição POST, realiza o salvamento da Role e retorna uma 
//resposta com o resultado da operação, indicando sucesso ou falha.