package com.capgemini.test1.configSecurity;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfiguration(JwtAuthenticationFilter jwtAuthenticationFilter, AuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/auth/login").permitAll()  // Autorise l'accès public au login
                        .requestMatchers("/auth/logout").authenticated()  // L'accès au logout est restreint aux utilisateurs authentifiés
                        //.requestMatchers("/users/me").authenticated()  // Tout utilisateur authentifié peut voir ses propres infos
                        .requestMatchers("/auth/adding-user").hasAuthority("ADMIN")
                        //I will.requestMatchers("/users/{id}").hasAuthority("ADMIN")  // Seul l'admin peut chercher, modifier, ou supprimer un utilisateur par ID
                        .requestMatchers("/users/**").hasAuthority("ADMIN")
                        .requestMatchers("grades/**").authenticated()
                        .requestMatchers("sites/**").authenticated()
                        .requestMatchers("technologies/**").authenticated()
                        .requestMatchers("status/**").authenticated()
                        .requestMatchers("collaborateurs/**").authenticated()
                        .requestMatchers("statutscollab/**").authenticated()
                        .requestMatchers("rolescollab/**").authenticated()
                        .requestMatchers("projets/**").authenticated()
                        .requestMatchers("home/**").authenticated()

                        .anyRequest().authenticated()  // Toutes les autres routes nécessitent une authentification
                )
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)  // Gère les sessions sans état
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable());  // Désactive la protection CSRF pour les API stateless

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
