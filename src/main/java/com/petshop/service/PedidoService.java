package com.petshop.service;

import com.petshop.domain.Pedido;

public interface PedidoService {
    Pedido finalizarCompra(String username);
}
