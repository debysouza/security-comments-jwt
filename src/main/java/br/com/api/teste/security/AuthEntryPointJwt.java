package br.com.api.teste.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

//inicia quando um usuário nãp autenticado tenta acessar um recurso que requer autenticação
//Assinatura para indicar que é um componente que pode ser utilizado como dependencia em outros locais da aplicação
@Component

//implementação da interface AuthenticationEntryPoint na classe AuthEntryPointJwt cujo método commence segue comentado abaixo.
public class AuthEntryPointJwt implements AuthenticationEntryPoint {
	
	//vai receber um usuário verificar se ele tem acesso aquele recurso e em caso negativo lançará uma exceção conforme 
	//descrito abaixo
	private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

	@Override
	//commence  é chamado sempre que um usuário não autenticado tenta acessar um recurso protegido.
	//O método personaliza a resposta de não autorizado (HTTP 401).
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		
		//Primeiro, ele registra a mensagem da exceção de autenticação usando o logger
		//Em seguida, define o tipo de conteúdo da resposta como JSON e configura o status
		//da resposta como HTTP 401 (Não autorizado).
		logger.error("Não autorizado: {}", authException.getMessage());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

		
		//Cria um mapa contendo detalhes sobre a falha na autenticação, incluindo o status HTTP,
		//uma mensagem de erro e o caminho que foi solicitado.
		final Map<String, Object> body = new HashMap<>();
		body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
		body.put("error", "Unauthorized");
		body.put("message", authException.getMessage());
		body.put("path", request.getServletPath());

		
		//Por último, usa o Jackson ObjectMapper para serializar 
		//esse mapa em JSON e escreve a saída no corpo da resposta HTTP.
		final ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(response.getOutputStream(), body);
	}

}