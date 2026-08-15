package com.petshop.service;

import com.petshop.domain.Producto;
import java.util.List;

public interface ProductoService {
    
    public List<Producto> getProductos();

    public List<Producto> getProductosActivos();
    
    public Producto getProducto(Producto producto);

    public Producto getProductoActivo(Integer idProducto);
    
    public void save(Producto producto);
    
    public void delete(Producto producto);
}
