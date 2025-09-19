package org.esfe.UsuarioApi.config;

import org.esfe.UsuarioApi.seguridad.configuracion.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private AuthenticationProvider authProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf->csrf.disable())
                .authorizeHttpRequests(authRequest -> authRequest
                        // ✅ RUTAS PÚBLICAS - SOLO LO ESENCIAL
                        .requestMatchers(
                                "/",                                           // Hola mundo
                                "/hola/**",                                    // Endpoints de prueba
                                "/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html", // Swagger
                                "/api/auth/login",                             // ✅ LOGIN PÚBLICO
                                "/api/auth/logout",                            // ✅ LOGOUT PÚBLICO
                                "/api/auth/registrar",                         // ✅ REGISTRO PÚBLICO
                                "/api/auth/validate",                          // ✅ VALIDACIÓN PÚBLICA
                                "/api/auth/debug-**",                          // Endpoints de debug
                                "/actuator/health",                            // Health check para gateway
                                "/error"                                       // Manejo de errores
                        ).permitAll()
                        // ✅ TODO LO DEMÁS REQUIERE AUTENTICACIÓN
                        .requestMatchers("/api/roles/**").authenticated()      // Roles protegidos
                        .requestMatchers("/api/usuarios/**").authenticated()   // Usuarios protegidos
                        .anyRequest().authenticated()                          // Cualquier otra ruta
                )
                .sessionManagement(sessionManager ->
                        sessionManager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // ✅ CORS PERMISIVO PARA GATEWAY Y ACCESO DIRECTO
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:3000",     // Frontend local
                "http://localhost:4200",
                "http://localhost:8080",     // Gateway local típico
                "http://localhost:8081",
                "http://localhost:8761",     // Eureka
                "http://localhost:54042"     // Tu puerto actual
        ));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(Arrays.asList("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}

/*
 * ✅ CONFIGURACIÓN PARCIAL:
 * - /api/auth/** → PÚBLICO (login, logout, registro, validación)
 * - /api/roles/** → REQUIERE AUTENTICACIÓN
 * - /api/usuarios/** → REQUIERE AUTENTICACIÓN
 * - Swagger y health checks → PÚBLICO
 *
 * Esto permite:
 * 1. Gateway puede hacer health checks
 * 2. Frontend puede hacer login/logout sin token
 * 3. Operaciones CRUD requieren JWT válido
 */