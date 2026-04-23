package com.obras.calculadora;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Calculadora de Materiais para Obra Residencial")
                        .version("1.0.0")
                        .description("""
                                API REST para cálculo de materiais em obras residenciais.
                                
                                **Etapa 2** — Calcula o volume de concreto nas vigas baldrame.
                                
                                **Etapa 3** — Calcula a quantidade de tijolos nas paredes.
                                
                                Disciplina: Desenvolvimento de Sistemas — UniCEUB
                                """)
                        .contact(new Contact()
                                .name("Ciência da Computação — UniCEUB")));
    }
}
