package com.petshop.service;

import com.petshop.domain.Usuario;
import com.petshop.repository.UsuarioRepository;
import com.petshop.repository.UsuarioRolRepository;
import java.util.List;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioRolRepository usuarioRolRepository;

    public UsuarioDetailsService(UsuarioRepository usuarioRepository, UsuarioRolRepository usuarioRolRepository) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioRolRepository = usuarioRolRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        List<SimpleGrantedAuthority> authorities = usuarioRolRepository.findRolesByUsername(username).stream()
                .map(usuarioRol -> new SimpleGrantedAuthority("ROLE_" + usuarioRol.getRol().getRol()))
                .toList();

        return User.withUsername(usuario.getUsername())
                .password(usuario.getPassword())
                .authorities(authorities)
                .disabled(!Boolean.TRUE.equals(usuario.getActivo()))
                .build();
    }
}
