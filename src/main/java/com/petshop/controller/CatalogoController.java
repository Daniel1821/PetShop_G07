package com.petshop.controller;

import com.petshop.service.ProductoService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class CatalogoController {

    private final ProductoService productoService;

    public CatalogoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping("/catalogo")
    public String catalogo(Model model) {
        model.addAttribute("productos", productoService.getProductosActivos());
        return "catalogo/listado";
    }

    @GetMapping("/catalogo/{idProducto}")
    public String detalle(@PathVariable Integer idProducto, Model model) {
        var producto = productoService.getProductoActivo(idProducto);
        if (producto == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado");
        }
        model.addAttribute("producto", producto);
        return "catalogo/detalle";
    }
}
