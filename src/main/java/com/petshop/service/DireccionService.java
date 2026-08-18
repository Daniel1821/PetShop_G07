package com.petshop.service;

import com.petshop.domain.Direccion;
import java.util.List;

public interface DireccionService {
    List<Direccion> obtenerDirecciones(String username);
    Direccion obtenerDireccion(String username, Integer idDireccion);
    void guardar(String username, Direccion direccion);
    void eliminar(String username, Integer idDireccion);
}
