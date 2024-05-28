// CLASSE AUXILIAR DE CONFIGURAÇÃO ESPECÍFICA PARA O PERFIL DE TESTE PARA INSTANCIAR O BANCO DE DADOS

package com.proton.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("http://localhost:3000", "https://projeto-protoon-front.vercel.app/", "https://proto-on-beta.vercel.app/",
            "https://main--resonant-rugelach-e9d9de.netlify.app")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Adicionando suporte para o método OPTIONS
            .allowedHeaders("*")
            .allowCredentials(true)
            .maxAge(3600); // Definindo o tempo de vida do preflight request para 1 hora
    }
}