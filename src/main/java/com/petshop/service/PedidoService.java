package com.petshop.service;

import com.petshop.domain.Pedido;
import java.util.List;
import java.time.LocalDate;

public interface PedidoService {
    Pedido finalizarCompra(String username, Integer idDireccion);
    List<Pedido> obtenerPedidosCliente(String username);
    List<Pedido> obtenerPedidos();
    void actualizarEstado(Integer idPedido, Pedido.Estado estado);
    List<Pedido> obtenerPedidosEntre(LocalDate inicio, LocalDate fin);
    Pedido obtenerPedidoCliente(Integer idPedido, String username);
}
