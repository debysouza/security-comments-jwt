package br.com.api.teste.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

@Configuration //indica que a classe será inicializada junto com a aplicação
//@SecurityScheme-> usado para definir o schema de segurança da API
@SecurityScheme(name = "Bearer Auth", //atribui um nome para o schema de segurança. Este deve ser o mesmo passado na propriedade name da anotação @SecurityRequirement que fica no controller.
				type = SecuritySchemeType.HTTP, //define o tipo do schema de segurança, utilizado para Bearer
				bearerFormat = "JWT", //String que especifica a formatação do token
				scheme = "bearer") //especifica o esquema para autenticação por token, nesse caso, o bearer
public class SwaggerConfig {//classe para configurar o Swagger na aplicação

	@Value("${prop.swagger.dev-url}")//serve para que você possa capturar o valor de alguma propriedade definida no application.properties
	private String devUrl;

	@Value("${prop.swagger.prod-url}")
	private String prodUrl;

	@Bean//informa para o Spring que você quer criar esse objeto e deixar ele disponível para outras classes utilizarem ele como dependência
	public OpenAPI myOpenAPI() {//OpenAPI-> é uma especificação aberta que define uma descrição de interface padrão para API HTTP
		//Contrução de objetos Server, utilizado para especificar o(s) paths(parte da URL que aparece antes da URN /swagger-ui) utilizados nas requisições API
		Server devServer = new Server();
		devServer.setUrl(devUrl);
		devServer.setDescription("Server URL - ambiente de desenvolvimento");

		Server prodServer = new Server();
		prodServer.setUrl(prodUrl);
		prodServer.setDescription("Server URL - ambiente de produção");

		//Construção do objeto Contact, utilizado para passar as informações de contato dos responsáveis pela aplicação
		Contact contact = new Contact();
		contact.setEmail("teste@gmail.com");
		contact.setName("Teste");
		contact.setUrl("https://www.teste.com");

		//Construção do objeto License, utilizado para apresentar, na documentação do Swagger, a License da aplicação
		License apache = new License().name("Apache License Version 2.0")
				.url("https://www.apache.org/license/LICENSE-2.0");

		//Construção do objeto Info, utilizado para fazer o cabeçalho na documentação do Swagger
		Info info = new Info().title("Documentação API - JWT").version("1.0.1").contact(contact)
				.description("API com endpoints com JWT para testes.")
				.termsOfService("https://www.teste.com/terms").license(apache);

		//retorna o objeto OpenApi com as configurações acima definidas
		return new OpenAPI().info(info).servers(List.of(devServer, prodServer));
	}

}
