package com.edu.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login").permitAll()
                        // Console H2 restrito apenas a ADMIN (não mais público)
                        .requestMatchers("/h2-console/**").hasRole("ADMIN")
                        // Operações de escrita e troca de status restritas a ADMIN no servidor
                        .requestMatchers(
                                "/web/linhas/nova",
                                "/web/linhas/salvar",
                                "/web/linhas/editar/**",
                                "/web/linhas/excluir/**",
                                "/web/viagens/*/status"
                        ).hasRole("ADMIN")
                        // Consulta liberada para ADMIN e OPERADOR
                        .requestMatchers("/", "/web/linhas", "/web/viagens", "/403").hasAnyRole("ADMIN", "OPERADOR")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/web/linhas", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                .exceptionHandling(ex -> ex
                        .accessDeniedPage("/403")
                )
                .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"))
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
        UserDetails admin = User.builder()
                .username("admin")
                .password(encoder.encode("admin123"))
                .roles("ADMIN")
                .build();

        UserDetails operador = User.builder()
                .username("operador")
                .password(encoder.encode("oper123"))
                .roles("OPERADOR")
                .build();

        return new InMemoryUserDetailsManager(admin, operador);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}