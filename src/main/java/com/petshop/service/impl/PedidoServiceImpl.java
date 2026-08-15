package com.petshop.service.impl;

import com.petshop.domain.CarritoDetalle;
import com.petshop.domain.DetallePedido;
import com.petshop.domain.Direccion;
import com.petshop.domain.Pedido;
import com.petshop.domain.Producto;
import com.petshop.domain.Usuario;
import com.petshop.repository.CarritoDetalleRepository;
import com.petshop.repository.DireccionRepository;
import com.petshop.repository.PedidoRepository;
import com.petshop.repository.ProductoRepository;
import com.petshop.repository.UsuarioRepository;
import com.petshop.service.PedidoService;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PedidoServiceImpl implements PedidoService {
    private final CarritoDetalleRepository carritoDetalleRepository;
    private final DireccionRepository direccionRepository;
    private final PedidoRepository pedidoRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;

    public PedidoServiceImpl(CarritoDetalleRepository carritoDetalleRepository, DireccionRepository direccionRepository,
            PedidoRepository pedidoRepository, ProductoRepository productoRepository, UsuarioRepository usuarioRepository) {
        this.carritoDetalleRepository = carritoDetalleRepository;
        this.direccionRepository = direccionRepository;
        this.pedidoRepository = pedidoRepository;
        this.productoRepository = productoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional
    public Pedido finalizarCompra(String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("Usuario no encontrado."));
        Direccion direccion = direccionRepository.findFirstByUsuarioUsernameAndPredeterminadaTrue(username)
                .orElseThrow(() -> new IllegalStateException("Debes registrar una dirección predeterminada antes de comprar."));
        List<CarritoDetalle> carrito = carritoDetalleRepository.findCarritoByUsername(username);
        if (carrito.isEmpty()) {
            throw new IllegalStateException("Tu carrito está vacío.");
        }

        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setDireccion(direccion);
        pedido.setEstado(Pedido.Estado.Pendiente);
        BigDecimal total = BigDecimal.ZERO;

        for (CarritoDetalle item : carrito) {
            Producto producto = item.getProducto();
            validarProducto(producto, item.getCantidad());
            producto.setExistencias(producto.getExistencias() - item.getCantidad());
            productoRepository.save(producto);

            DetallePedido detalle = new DetallePedido();
            detalle.setPedido(pedido);
            detalle.setProducto(producto);
            detalle.setPrecioHistorico(producto.getPrecio());
            detalle.setCantidad(item.getCantidad());
            pedido.getDetalles().add(detalle);
            total = total.add(producto.getPrecio().multiply(BigDecimal.valueOf(item.getCantidad())));
        }

        pedido.setTotal(total);
        Pedido pedidoGuardado = pedidoRepository.save(pedido);
        carritoDetalleRepository.deleteAll(carrito);
        return pedidoGuardado;
    }

    private void validarProducto(Producto producto, Integer cantidad) {
        if (!Boolean.TRUE.equals(producto.getActivo()) || producto.getPrecio() == null
                || producto.getExistencias() == null || cantidad == null || cantidad < 1
                || producto.getExistencias() < cantidad) {
            throw new IllegalStateException("Uno de los productos ya no cuenta con existencias suficientes. Actualiza tu carrito.");
        }
    }
}
