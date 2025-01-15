package com.example.demo.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        /* return new OpenAPI()
                .info(new Info()
                        .title("API Demo")
                        .version("1.0.0")
                        .description("Documentação da API com Springdoc OpenAPI"));

         */

        Server server = new Server();
        server.setUrl("http://localhost:8080");
        server.setDescription("Development");

        Contact myContact = new Contact();
        myContact.setName("Renato Nunes");
        myContact.setEmail("renato97.dj@gmail.com");

        Info information = new Info()
                .title("System API")
                .version("1.0")
                .description("This API exposes endpoints.")
                .contact(myContact);
        return new OpenAPI().info(information).servers(List.of(server));


    }
}
