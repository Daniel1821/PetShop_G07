package com.petshop.controller;

import com.petshop.service.PedidoService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PedidoController {
    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping("/pedidos")
    public String historial(Authentication authentication, Model model) {
        model.addAttribute("pedidos", pedidoService.obtenerPedidosCliente(authentication.getName()));
        return "pedidos/historial";
    }

    @GetMapping("/pedidos/{idPedido}/comprobante")
    public String comprobante(Authentication authentication, @PathVariable Integer idPedido, Model model) {
        var pedido = pedidoService.obtenerPedidoCliente(idPedido, authentication.getName());
        if (pedido == null) return "redirect:/pedidos";
        model.addAttribute("pedido", pedido);
        return "pedidos/comprobante";
    }
}
