package com.petshop.controller;

import com.petshop.domain.Pedido;
import com.petshop.service.PedidoService;
import com.petshop.service.ReporteService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ReporteController {
    private final PedidoService pedidoService;
    private final ReporteService reporteService;
    public ReporteController(PedidoService pedidoService, ReporteService reporteService) { this.pedidoService = pedidoService; this.reporteService = reporteService; }
    @GetMapping("/reportes/ventas")
    public String ventas(@RequestParam(required = false) LocalDate inicio, @RequestParam(required = false) LocalDate fin, Model model) {
        List<Pedido> pedidos = pedidoService.obtenerPedidosEntre(inicio, fin);
        model.addAttribute("pedidos", pedidos); model.addAttribute("inicio", inicio); model.addAttribute("fin", fin);
        model.addAttribute("total", pedidos.stream().map(Pedido::getTotal).reduce(BigDecimal.ZERO, BigDecimal::add));
        model.addAttribute("productosMasVendidos", reporteService.productosMasVendidos());
        return "reportes/ventas";
    }
    @GetMapping("/reportes/ventas/csv")
    public ResponseEntity<String> csv(@RequestParam(required = false) LocalDate inicio, @RequestParam(required = false) LocalDate fin) {
        StringBuilder csv = new StringBuilder("Pedido,Fecha,Cliente,Estado,Total\n");
        for (Pedido p : pedidoService.obtenerPedidosEntre(inicio, fin)) csv.append(p.getIdPedido()).append(',').append(p.getFecha()).append(',').append(p.getUsuario().getNombre()).append(' ').append(p.getUsuario().getApellidos()).append(',').append(p.getEstado()).append(',').append(p.getTotal()).append('\n');
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=ventas.csv").contentType(MediaType.parseMediaType("text/csv")).body(csv.toString());
    }
}
