package com.github.rayinfinite.auth.config.security;

import com.fasterxml.jackson.core.json.JsonWriteFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    private final String[] allowedMethods = {"GET", "POST", "PUT", "PATCH", "OPTIONS", "DELETE"};
    private final String[] allowedHeaders = {
            "Authorization",
            "Accept",
            "X-Requested-With",
            "X-XSRF-Token",
            "Content-Type",
            "Access-Control-Request-Method",
            "Access-Control-Request-Headers"};
    private final String[] exposedHeaders = {
            "Access-Control-Allow-Origin",
            "Access-Control-Allow-Credentials"};

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOriginPattern("*");
        config.setAllowedMethods(List.of(allowedMethods));
        config.setAllowedHeaders(List.of(allowedHeaders));
        config.setExposedHeaders(List.of(exposedHeaders));
        config.setMaxAge(3600L);
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    /**
     * Global Configuration Spring Controller Serialisation
     */
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        // Formatted all numeric related types as strings
        objectMapper.configure(JsonWriteFeature.WRITE_NUMBERS_AS_STRINGS.mappedFeature(), true);
        // No exception is thrown when deserialising json when extra fields are passed in.
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }
}
