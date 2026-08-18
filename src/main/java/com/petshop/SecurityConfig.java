package com.petshop;

import com.petshop.service.UsuarioDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final UsuarioDetailsService usuarioDetailsService;

    public SecurityConfig(UsuarioDetailsService usuarioDetailsService) {
        this.usuarioDetailsService = usuarioDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.userDetailsService(usuarioDetailsService)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/index", "/login", "/registro/**", "/recuperar-contrasena/**", "/catalogo/**", "/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/carrito/**").hasRole("CLIENTE")
                        .requestMatchers("/direcciones/**").hasRole("CLIENTE")
                        .requestMatchers("/pedidos/gestion/**").hasRole("ADMIN")
                        .requestMatchers("/reportes/**").hasRole("ADMIN")
                        .requestMatchers("/pedidos/**").hasRole("CLIENTE")
                        .requestMatchers("/usuarios/**").hasRole("ADMIN")
                        .requestMatchers("/productos/nuevo", "/productos/guardar", "/productos/modificar/**", "/productos/eliminar/**").hasAnyRole("ADMIN", "VENDEDOR")
                        .requestMatchers("/categorias/nuevo", "/categorias/guardar", "/categorias/modificar/**", "/categorias/eliminar/**").hasAnyRole("ADMIN", "VENDEDOR")
                        .requestMatchers("/productos", "/categorias").hasAnyRole("ADMIN", "VENDEDOR")
                        .anyRequest().authenticated())
                .formLogin(form -> form.loginPage("/login").defaultSuccessUrl("/", true).permitAll())
                .logout(logout -> logout.logoutSuccessUrl("/login?logout").permitAll());
        return http.build();
    }
}
