package com.petshop.service.impl;

import com.petshop.domain.Producto;
import com.petshop.repository.ProductoRepository;
import com.petshop.service.ProductoService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Producto> getProductos() {
        return productoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Producto> getProductosActivos() {
        return productoRepository.findByActivoTrueOrderByDescripcionAsc();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Producto> buscarProductosActivos(String busqueda, Integer idCategoria) {
        String termino = busqueda == null ? null : busqueda.trim();
        if (termino != null && termino.isEmpty()) {
            termino = null;
        }
        return productoRepository.buscarProductosActivos(termino, idCategoria);
    }

    @Override
    @Transactional(readOnly = true)
    public Producto getProducto(Producto producto) {
        return productoRepository.findById(producto.getIdProducto()).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Producto getProductoActivo(Integer idProducto) {
        return productoRepository.findByIdProductoAndActivoTrue(idProducto).orElse(null);
    }

    @Override
    @Transactional
    public void save(Producto producto) {
        productoRepository.save(producto);
    }

    @Override
    @Transactional
    public void delete(Producto producto) {
        productoRepository.delete(producto);
    }
}
