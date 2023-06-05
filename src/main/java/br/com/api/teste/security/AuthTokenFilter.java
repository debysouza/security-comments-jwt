package br.com.api.teste.security;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.api.teste.security.jwt.JwtUtils;
import br.com.api.teste.security.services.UserDetailsServiceImpl;

//AuthTokenFilter -> filtro de autenticações que implementa a interface OncePerRequestFilter com base em um token JWT (JSON Web Token).
public class AuthTokenFilter extends OncePerRequestFilter {
	@Autowired
	private JwtUtils jwtUtils;

	@Autowired
	private UserDetailsServiceImpl userDetailsService;

	// registar logs
	private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);
	
	// doFilterInternal -> interceptar as requisições recebidas pelo servidor e realizar a autenticação do usuário
	@Override
	protected void doFilterInternal(
			
			// servlet -> "pequeno servido"  que recebe requisições HTTP, processa e responde ao cliente
			HttpServletRequest request, //provê informação de requests para http servlet que cria um objeto e passa como argumento para o serviço servlet (doGet, do Post...)
			HttpServletResponse response, // provê funções http, como acessar cabeçalho ou cookies
			FilterChain filterChain) //encadeia filtros
			throws ServletException, IOException {
		try {
						
			// chama o método descrito abaixo
			String jwt = parseJwt(request);
			
			// verifica so o token não é nulo e se e é valido usado instancia do utils
			if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
				// se valido, o filtro extrai o nome do usuário no token pela função utils
				 
				// extrai o nome do user no token, pois o usuário autenticado está dentro do token
				String username = jwtUtils.getUserNameFromJwtToken(jwt);

				//  carrega os detalhes do usuário utilizando o serviço UserDetailsServiceImpl 
				UserDetails userDetails = userDetailsService.loadUserByUsername(username);
				
				// contém os detalhes do usuário, nenhuma senha e as autorizações
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				
				// define os detalhes da autenticação com base na solicitação atual 
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				// a autenticação é amazenada no contexto de segurança da sessão do usuário
				SecurityContextHolder.getContext().setAuthentication(authentication);
				
				
			}
		} catch (Exception e) {
			// Caso o usuário não consiga se autenticas é registrado um erro no logger
			logger.error("Não foi possível realizar a autenticação do usuário: {}", e);
		}
		
		// por fim, o filtro chama essa função para permitir que a solicitação
		//prossiga para o próximo filtro na cadeia ou para o controlador adequado
		filterChain.doFilter(request, response);
	}
	
	// extrai o token JWT do cabeçalho de autorização da solicitação
	private String parseJwt(HttpServletRequest request) {
		
		// extrai o cabeçalho de autenticação para buscar a autorização
		String headerAuth = request.getHeader("Authorization");

		// verifica se a string contém texto e a palavra Bearer
		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
			
			// caso positivo, é removida a palavra Bearer e o espaço ficando somente com o token
			return headerAuth.substring(7, headerAuth.length());
		}

		// caso não seja encontrado o token, a resposta é nula
		return null;
	}
	
	
	/*
	 * Em resumo, essa classe é responsável por extrair o token JWT de uma
	 * solicitação HTTP, validar o token, carregar os detalhes do usuário com base
	 * no token e autenticar o usuário no contexto de segurança do Spring.
	 */

}