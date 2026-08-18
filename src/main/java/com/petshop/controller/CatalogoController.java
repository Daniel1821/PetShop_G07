package com.petshop.controller;

import com.petshop.service.ProductoService;
import com.petshop.service.CategoriaService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class CatalogoController {

    private final ProductoService productoService;
    private final CategoriaService categoriaService;

    public CatalogoController(ProductoService productoService, CategoriaService categoriaService) {
        this.productoService = productoService;
        this.categoriaService = categoriaService;
    }

    @GetMapping("/catalogo")
    public String catalogo(@RequestParam(required = false) String busqueda,
            @RequestParam(required = false) Integer categoria, Authentication authentication, Model model) {
        model.addAttribute("productos", productoService.buscarProductosActivos(busqueda, categoria));
        model.addAttribute("categorias", categoriaService.getCategoriasActivas());
        model.addAttribute("busqueda", busqueda);
        model.addAttribute("categoriaSeleccionada", categoria);
        boolean esAdmin = authentication != null && authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
        model.addAttribute("esAdmin", esAdmin);
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
