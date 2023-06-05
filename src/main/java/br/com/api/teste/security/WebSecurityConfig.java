package br.com.api.teste.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import br.com.api.teste.security.services.UserDetailsServiceImpl;

//Anotação que indica que a classe é uma classe de configuração
@Configuration
//Passa ao Spring que você deseja habilitar as anotações relacionadas aos métodos de segurança
@EnableMethodSecurity(prePostEnabled = true)
//Método que habilita a configuração de segurança da aplicação
@EnableWebSecurity
public class WebSecurityConfig {
	// injeção da implementação de interface que é usada para carregar os detalhes do usuário durante a auteticação
	@Autowired
	UserDetailsServiceImpl userDetailsService;

	// injeção de outra implementação de interface que é usadda para tratar o ponto de entrada de autenticações
	@Autowired
	private AuthEntryPointJwt unauthorizedHandler;

	// anotação para criação de um objeto que fique disponível para a utilização em outra classes, como uma dependência
	@Bean
	//classe para filtro de atenticação da requisição
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		// cors = permite que diferentes requisições possam ser feitas de diferentes origens
		http.cors(Customizer.withDefaults()).csrf(csrf -> csrf.disable())
				// define um ponto de entrada para a autenticação
				.exceptionHandling(handling -> handling.authenticationEntryPoint(unauthorizedHandler))
				// define uma política da criação de sessão no Spring. O StateLess indica que nenhuma criação sera criada pelo Spring para rastrear o estado do usuário e armazenar informações entre diversas solicitações.
				.sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				// método para definir quem vai ter quais permissões
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/g6/**", "/auth/**", "/h2-console/**", "/roles/**", "/test/all/**",
								// permitAll => permite acesso a url para todos os papeis
								"/swagger-ui/**", "/v3/api-docs/**", "/actuator/**").permitAll()
						// requestMatchers => definem padrões de URL
						.requestMatchers("/test/user/**").hasAnyRole("USER", "ADMIN")// HasAnyRole => permite acesso a url para um ou mais papeis quando não há interesse de serem todos
						.requestMatchers("/test/admin/**").hasRole("ADMIN")// HasRole => permite acesso a url para um papel específico
						// define a política de segurança, concluindo que apenas as solicitações anteriores poderão ser requisitadas
						.anyRequest().authenticated());

		// provedpr que autentica os usuários de acordo com suas credenciais
		http.authenticationProvider(authenticationProvider());

		// filtro de autenticação responsável por interceptar as solicitações HTTp e fazer a verificação do Token JWT
		http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

	@Bean
	// método de configuração do cors, o mesmo responsável por liberar que diferentes requisições possam ser feitas
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("*"));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT"));
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	@Bean
	// filtro que verifica o Token Jwt passado no cabeçalho da requisição
	public AuthTokenFilter authenticationJwtTokenFilter() {
		return new AuthTokenFilter();
	}

	@Bean
	// método para autenticação do usuário de acordo com as informações já armazenadas e codificador de senha para verificar a autenticidade da mesma
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());

		return authProvider;
	}

	@Bean
	// responsável geral pela autenticação, esse método que retorna o objeto responsável pela liberação ou não
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

	@Bean
	// método responsável por codificar a senha fornecida pelo usuário
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}