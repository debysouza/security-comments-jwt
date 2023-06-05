package br.com.api.teste.security.domain;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity //representa as classes essenciais, chamados beans
@Table //criação de tabela no banco
//propriedade name-> indica o nome da tabela
//propriedade uniqueConstraints-> indica restrições na tabela
//anotação @UniqueConstraint-> indica a restrição específica passando a coluna como propriedade, ou seja, a coluna referenciada é um valor único na tabela
	(name = "users", uniqueConstraints = {
		@UniqueConstraint(columnNames = "username"),
		@UniqueConstraint(columnNames = "email") 
	})
//a classe representa um modelo/entidade de User que contém informações para autenticação do usuário no sistema
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@NotBlank //atributo não pode ser vazio
	@Size(max = 20) //define uma restrição para o tamanho da coluna
	private String username;

	@NotBlank
	@Size(max = 50)
	@Email //valida se a variável abaixo está na estrutura de e-mail
	private String email;

	@NotBlank
	@Size(max = 120)
	private String password;

	//propriedade fetch = FetchType.LAZY-> serve para carregar as informações do banco pela primeira vez, ou seja, em tempo de execução
	//LAZY x EAGER-> Eager carrega os dados mesmo sem a necessidade de uso, equanto que o Lazy carrega os dados "filtrados", ou seja, sob demanda
	@ManyToMany(fetch = FetchType.LAZY) //@ManyToMany -> estabelece uma relação de muitos para muitos entre duas entidades/classes
	//@JoinTable-> especifica a tabela de junção que armazena o relacionamento entre as especificadas nas propriedades joinColumns e inverseJoinColumns
	//@JoinColumn-> especifica a coluna que faz referência a tabela definida na propriedade name da respectiva anotação, colunas user_id e role_id
	//inverseJoinColumns-> especifica a coluna que faz referência a tabela user_roles(coluna role_id)
	@JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	//Set-> representa as roles atribuídas ao usuário
	//Set-> um conjunto de elementos não repetidos
	//HashSet-> efetua busca mais rápida e eficiente
	//HashSet - LinkedHashSet - TreeSet(valores ordenados)
	private Set<Role> roles = new HashSet<>();

	//Construtores
	public User() {
	}

	public User(String username, String email, String password) {
		this.username = username;
		this.email = email;
		this.password = password;
	}

	//Getters e Setters
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
}