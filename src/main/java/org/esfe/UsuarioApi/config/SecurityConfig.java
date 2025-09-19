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
                        // ✅ RUTAS PÚBLICAS - ESPECIALMENTE PARA GATEWAY
                        .requestMatchers(
                                // Endpoints básicos
                                "/",
                                "/hola/**",

                                // Swagger y documentación
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-ui.html",

                                // ✅ AUTENTICACIÓN - LO MÁS IMPORTANTE PARA EL GATEWAY
                                "/api/auth/login",
                                "/api/auth/logout",
                                "/api/auth/registrar",
                                "/api/auth/validate",
                                "/api/auth/debug-**",

                                // ✅ HEALTH CHECKS - CRÍTICO PARA QUE EL GATEWAY DETECTE EL SERVICIO
                                "/actuator/**",
                                "/health",

                                // Manejo de errores
                                "/error"
                        ).permitAll()

                        // ✅ RESTO REQUIERE AUTENTICACIÓN (puede venir del Gateway)
                        .anyRequest().authenticated()
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

        // ✅ CONFIGURACIÓN CORS PARA MICROSERVICIOS Y GATEWAY
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:3000",                    // Frontend local
                "http://localhost:8080",                    // Gateway local
                "http://localhost:8081",
                "http://localhost:8082",
                "https://*.onrender.com",                   // Servicios en Render
                "https://api-gateway-*.onrender.com",       // Gateway en la nube
                "https://eureka-server-1jnn.onrender.com"   // Eureka server
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
 * ✅ CONFIGURACIÓN OPTIMIZADA PARA GATEWAY:
 *
 * RUTAS PÚBLICAS:
 * - /api/auth/** → Para que el frontend pueda hacer login/logout
 * - /actuator/** → Para que el gateway pueda hacer health checks
 * - /health → Endpoint de salud
 *
 * RUTAS PROTEGIDAS:
 * - /api/usuarios/** → Requieren JWT
 * - /api/roles/** → Requieren JWT
 *
 * El gateway puede:
 * 1. Detectar el servicio via Eureka
 * 2. Hacer health checks a /actuator/health
 * 3. Rutear las peticiones de auth sin problemas
 * 4. Pasar los JWT para las rutas protegidas
 */