package com.petshop.service.impl;

import com.petshop.domain.Usuario;
import com.petshop.repository.UsuarioRepository;
import com.petshop.service.UsuarioService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> getUsuarios() {
        return usuarioRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Usuario getUsuario(Usuario usuario) {
        return usuarioRepository.findById(usuario.getIdUsuario()).orElse(null);
    }

    @Override
    @Transactional
    public void save(Usuario usuario) {
        usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public void delete(Usuario usuario) {
        usuarioRepository.delete(usuario);
    }

    @Override
    @Transactional
    public void restablecerPassword(String correo, String password) {
        if (correo == null || correo.isBlank() || password == null || password.length() < 3) {
            throw new IllegalArgumentException("Ingresa un correo válido y una contraseña de al menos 3 caracteres.");
        }
        Usuario usuario = usuarioRepository.findByCorreo(correo.trim())
                .orElseThrow(() -> new IllegalArgumentException("No existe una cuenta con ese correo."));
        usuario.setPassword(passwordEncoder.encode(password));
        usuarioRepository.save(usuario);
    }
}
