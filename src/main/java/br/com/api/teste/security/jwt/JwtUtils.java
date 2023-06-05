package br.com.api.teste.security.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import br.com.api.teste.security.services.UserDetailsImpl;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component //indica que a classe é um componente e gerenciada pelo container IOC(Inversão de Controle)
//contém métodos úteis: gerar token, retornar nome de usuário pelo token e validação do token
public class JwtUtils {
	//cria um objeto logger responsável por mostrar(printar) o erro no méto de validação do token
	private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

	//@Value cria uma string "${app.jwt.secret}" definida no application.properties
	@Value("${app.jwt.secret}")
	//utilizado para geração da validação de token - token necessário para autenticar
	private String jwtSecret;

	//@Value cria uma string "${app.jwt.expiration.ms}" definida no application.properties
	@Value("${app.jwt.expiration.ms}")
	//duração de tempo em milissegundo para expiração do token - token gerado
	private int jwtExpirationMs;
	
	//responsável por gerar o token a partir do objeto que representa o usuário autenticado - authentication
	public String generateJwtToken(Authentication authentication) {

		//cria uma instância de UserDetailsImpl para recuperar as informações principais do usuário autenticado
		UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
		//cria uma chave secreta a partir da variável jwtSecret
		//a classe Keys gera a chave secreta de acordo com o método hmacShaKeyFor()
		//jwtSecret.getBytes(StandardCharsets.UTF_8)-> indica que a chave secreta são bytes da String jwtSecret codificados em UTF-8
		SecretKey sKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
		
		//Jwts utilizado para construir o token
		//informa as configurações de construção do token
		return Jwts.builder()
					.setSubject((userPrincipal.getUsername()))//recupera o username do usuário principal
					.setIssuedAt(new Date())//data de emissão do token
					.setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))//data de expiração do token somado ao tempo em milessegundos definido na variável jwtExpirationMs 
					.signWith(sKey)//recebe a chave como parâmetro e assina o token
					.compact();//compacta e retorna o token como String
	}

	//retorna um nome de usuário a partir do token
	public String getUserNameFromJwtToken(String token) {
		SecretKey sKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
		return Jwts.parserBuilder()//retorna um obejto que permite configurar o parse de tokens
				.setSigningKey(sKey)//recebe a chave como parâmetro e configura o token
				.build()//cria o objeto com as informações acima
				.parseClaimsJws(token)//verifica a assinatura utilizando a chave secreta configurada
				.getBody().getSubject();//recupera o username do corpo do token(definido no método acima)
	}

	//retorna true ou false para a autenticação do token
	public boolean validateJwtToken(String authToken) {
		try {
			SecretKey sKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
			Jwts.parserBuilder()
				.setSigningKey(sKey)
				.build()
				.parseClaimsJws(authToken)//verifica a assinatura do token
				.getBody()//retorna as informações do objeto
				.getSubject();//retorna o username
			return true;//situação ok
		}catch (JwtException e) {//caso caia na exception JwtException
			logger.error("Token JWT inválido: {}", e.getMessage());//retorna false com a mensagem definida
		}
		return false;
	}
}