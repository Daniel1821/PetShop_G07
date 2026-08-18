package com.petshop.controller;

import com.petshop.domain.CarritoDetalle;
import com.petshop.service.CarritoService;
import com.petshop.service.PedidoService;
import com.petshop.service.DireccionService;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CarritoController {
    private final CarritoService carritoService;
    private final PedidoService pedidoService;
    private final DireccionService direccionService;
    public CarritoController(CarritoService carritoService, PedidoService pedidoService, DireccionService direccionService) { this.carritoService = carritoService; this.pedidoService = pedidoService; this.direccionService = direccionService; }

    @GetMapping("/carrito")
    public String verCarrito(Authentication auth, Model model) {
        List<CarritoDetalle> detalles = carritoService.obtenerCarrito(auth.getName());
        BigDecimal total = detalles.stream().map(d -> d.getProducto().getPrecio().multiply(BigDecimal.valueOf(d.getCantidad()))).reduce(BigDecimal.ZERO, BigDecimal::add);
        model.addAttribute("detalles", detalles); model.addAttribute("total", total); model.addAttribute("direcciones", direccionService.obtenerDirecciones(auth.getName()));
        return "carrito/listado";
    }
    @PostMapping("/carrito/agregar")
    public String agregar(Authentication auth, @RequestParam Integer idProducto, @RequestParam(defaultValue = "1") Integer cantidad, RedirectAttributes flash) {
        try { carritoService.agregarProducto(auth.getName(), idProducto, cantidad); flash.addFlashAttribute("exito", "Producto agregado al carrito."); }
        catch (IllegalArgumentException e) { flash.addFlashAttribute("error", e.getMessage()); }
        return "redirect:/catalogo/" + idProducto;
    }
    @PostMapping("/carrito/{id}/cantidad")
    public String actualizar(Authentication auth, @PathVariable Integer id, @RequestParam Integer cantidad, RedirectAttributes flash) {
        try { carritoService.actualizarCantidad(auth.getName(), id, cantidad); flash.addFlashAttribute("exito", "Cantidad actualizada."); }
        catch (IllegalArgumentException e) { flash.addFlashAttribute("error", e.getMessage()); }
        return "redirect:/carrito";
    }
    @PostMapping("/carrito/{id}/eliminar")
    public String eliminar(Authentication auth, @PathVariable Integer id, RedirectAttributes flash) {
        try { carritoService.eliminarProducto(auth.getName(), id); flash.addFlashAttribute("exito", "Producto eliminado del carrito."); }
        catch (IllegalArgumentException e) { flash.addFlashAttribute("error", e.getMessage()); }
        return "redirect:/carrito";
    }

    @PostMapping("/carrito/finalizar")
    public String finalizar(Authentication auth, @RequestParam(required = false) Integer idDireccion, RedirectAttributes flash) {
        try {
            var pedido = pedidoService.finalizarCompra(auth.getName(), idDireccion);
            flash.addFlashAttribute("exito", "Compra realizada con éxito.");
            return "redirect:/pedidos/" + pedido.getIdPedido() + "/comprobante";
        } catch (IllegalStateException e) {
            flash.addFlashAttribute("error", e.getMessage());
            return "redirect:/carrito";
        }
    }
}
