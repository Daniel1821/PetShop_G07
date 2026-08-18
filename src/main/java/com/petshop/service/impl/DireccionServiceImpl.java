package com.petshop.service.impl;

import com.petshop.domain.Direccion;
import com.petshop.domain.Usuario;
import com.petshop.repository.DireccionRepository;
import com.petshop.repository.UsuarioRepository;
import com.petshop.service.DireccionService;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DireccionServiceImpl implements DireccionService {
    private final DireccionRepository direccionRepository;
    private final UsuarioRepository usuarioRepository;
    public DireccionServiceImpl(DireccionRepository direccionRepository, UsuarioRepository usuarioRepository) { this.direccionRepository = direccionRepository; this.usuarioRepository = usuarioRepository; }
    @Override @Transactional(readOnly = true)
    public List<Direccion> obtenerDirecciones(String username) { return direccionRepository.findByUsuarioUsernameOrderByPredeterminadaDescIdDireccionDesc(username); }
    @Override @Transactional(readOnly = true)
    public Direccion obtenerDireccion(String username, Integer idDireccion) { return direccionRepository.findByIdDireccionAndUsuarioUsername(idDireccion, username).orElse(null); }
    @Override @Transactional
    public void guardar(String username, Direccion direccion) {
        validar(direccion);
        Direccion destino = direccion.getIdDireccion() == null ? new Direccion() : direccionRepository.findByIdDireccionAndUsuarioUsername(direccion.getIdDireccion(), username).orElseThrow(() -> new IllegalArgumentException("La dirección no existe."));
        if (destino.getIdDireccion() == null) destino.setUsuario(usuarioRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado.")));
        destino.setProvincia(direccion.getProvincia().trim()); destino.setCanton(direccion.getCanton().trim()); destino.setDistrito(direccion.getDistrito().trim()); destino.setSenas(direccion.getSenas().trim()); destino.setTelefonoContacto(direccion.getTelefonoContacto());
        boolean predeterminada = Boolean.TRUE.equals(direccion.getPredeterminada()) || !direccionRepository.existsByUsuarioUsernameAndPredeterminadaTrue(username);
        if (predeterminada) direccionRepository.findByUsuarioUsernameOrderByPredeterminadaDescIdDireccionDesc(username).forEach(item -> item.setPredeterminada(false));
        destino.setPredeterminada(predeterminada); direccionRepository.save(destino);
    }
    @Override @Transactional
    public void eliminar(String username, Integer idDireccion) { direccionRepository.delete(direccionRepository.findByIdDireccionAndUsuarioUsername(idDireccion, username).orElseThrow(() -> new IllegalArgumentException("La dirección no existe."))); }
    private void validar(Direccion d) { if (vacio(d.getProvincia()) || vacio(d.getCanton()) || vacio(d.getDistrito()) || vacio(d.getSenas())) throw new IllegalArgumentException("Completa todos los datos obligatorios de la dirección."); }
    private boolean vacio(String valor) { return valor == null || valor.isBlank(); }
}
