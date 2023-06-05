package br.com.api.teste.security.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.api.teste.security.domain.User;
import br.com.api.teste.security.repositories.UserRepository;

@Service //informa que a classe é um serviço
//implementação da interface UserDetails que contém as informações de autenticação de usuário
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired //injeção do repositório de User
	UserRepository userRepository;

	@Override //sobrescrita de método
	@Transactional //indica uma transação simultânea no banco de dados
	//loadUserByUsername(String username)-> carrega o usuário pelo username
	//UsernameNotFoundException-> lançamento/tratamento de exceção quando username não for encontrado 
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//instância de User passando os valores de busca no banco(username)
		//orElseThrow()-> caso User não encontrado, dispara a mensagem definida no construtor do UsernameNotFoundException()
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

		//build-> instância de user
		return UserDetailsImpl.build(user);
	}

}