package com.ifba.tide.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        
        // Permite o envio de cookies ou cabeçalhos de autenticação
        config.setAllowCredentials(true);
        
        // Libera explicitamente as URLs do seu front-end (Vite e padrões do Vue)
        config.setAllowedOrigins(List.of("http://localhost:5173", "http://localhost:3000", "http://127.0.0.1:5173", "https://tide-app-gbk3.onrender.com"));
        
        // Libera todos os cabeçalhos (evita travamento por Content-Type, etc)
        config.setAllowedHeaders(List.of("*"));
        
        // Libera os métodos HTTP necessários para a sua API funcionar
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        
        // Aplica essa regra em todas as rotas da API
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}

