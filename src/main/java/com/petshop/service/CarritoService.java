package com.petshop.service;

import com.petshop.domain.CarritoDetalle;
import java.util.List;

public interface CarritoService {
    List<CarritoDetalle> obtenerCarrito(String username);
    void agregarProducto(String username, Integer idProducto, Integer cantidad);
    void actualizarCantidad(String username, Integer idCarritoDetalle, Integer cantidad);
    void eliminarProducto(String username, Integer idCarritoDetalle);
}
