package com.petshop.controller;

import com.petshop.domain.Pedido;
import com.petshop.service.PedidoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class GestionPedidoController {
    private final PedidoService pedidoService;

    public GestionPedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping("/pedidos/gestion")
    public String listado(Model model) {
        model.addAttribute("pedidos", pedidoService.obtenerPedidos());
        model.addAttribute("estados", Pedido.Estado.values());
        return "pedidos/gestion";
    }

    @PostMapping("/pedidos/gestion/{idPedido}/estado")
    public String actualizarEstado(@PathVariable Integer idPedido, @RequestParam Pedido.Estado estado,
            RedirectAttributes atributos) {
        try {
            pedidoService.actualizarEstado(idPedido, estado);
            atributos.addFlashAttribute("exito", "Estado del pedido actualizado.");
        } catch (IllegalArgumentException e) {
            atributos.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/pedidos/gestion";
    }
}
