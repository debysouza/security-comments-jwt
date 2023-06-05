package br.com.api.teste.security.services;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.api.teste.security.domain.User;

//serve para autenticar o usuário, saber as autorizações que ele possui e se ele está habilitado ou desabilitado
public class UserDetailsImpl implements UserDetails {
	//static-> pode ser utilizado sem instância, pode ser utilizado em qualquer classe desde que seja estática 
	//final-> indica que é uma constante
	//serialVersionUID-> indica uma serialização que indica que aquele objeto foi construído
	private static final long serialVersionUID = 1L;

	private Integer id;

	private String username;

	private String email;

	@JsonIgnore //ignora o formato Json
	private String password;

	//cria uma variável que pode armazenar uma coleção de objetos de qualquer tipo
	private Collection<? extends GrantedAuthority> authorities;

	//Construtor parametrizado
	public UserDetailsImpl(Integer id, String username, String email, String password,
			Collection<? extends GrantedAuthority> authorities) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.password = password;
		this.authorities = authorities;
	}

	//utiliza um método estático para construir o usuário
	public static UserDetailsImpl build(User user) {
		//GrantedAuthority-> representa uma autorização concedida ao user
		//cria uma lista do tipo GrantedAuthority que recebe as permissões do usuário
		//stream().map()-> para percorrer a lista de roles 
		//SimpleGrantedAuthority()-> armazenar o GrantedAuthority como uma String e associa ao usuário 
		List<GrantedAuthority> authorities = user.getRoles().stream()
				.map(role -> new SimpleGrantedAuthority(role.getName()
						.name())).collect(Collectors.toList());

		//retorna o objeto construído a partir da lista acima
		return new UserDetailsImpl(user.getId(), user.getUsername(), user.getEmail(), user.getPassword(), authorities);
	}

	//Getters e Setters-booleanos
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public Integer getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	//compara se o objeto recebido possui o mesmo id retornando true em caso positivo e false em caso de null
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		UserDetailsImpl user = (UserDetailsImpl) o;
		return Objects.equals(id, user.id);
	}
	
}
