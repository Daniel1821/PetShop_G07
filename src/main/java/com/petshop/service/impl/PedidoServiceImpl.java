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
import java.time.LocalDate;
import java.time.LocalTime;
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
    public Pedido finalizarCompra(String username, Integer idDireccion) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("Usuario no encontrado."));
        Direccion direccion = idDireccion == null
                ? direccionRepository.findFirstByUsuarioUsernameAndPredeterminadaTrue(username)
                        .orElseThrow(() -> new IllegalStateException("Debes registrar una dirección antes de comprar."))
                : direccionRepository.findByIdDireccionAndUsuarioUsername(idDireccion, username)
                        .orElseThrow(() -> new IllegalStateException("La dirección seleccionada no es válida."));
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

    @Override
    @Transactional(readOnly = true)
    public List<Pedido> obtenerPedidosCliente(String username) {
        return pedidoRepository.findByUsuarioUsernameOrderByFechaDesc(username);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pedido> obtenerPedidos() {
        return pedidoRepository.findAllWithUsuarioOrderByFechaDesc();
    }

    @Override
    @Transactional
    public void actualizarEstado(Integer idPedido, Pedido.Estado estado) {
        if (estado == null) {
            throw new IllegalArgumentException("Debes seleccionar un estado válido.");
        }
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new IllegalArgumentException("El pedido no existe."));
        pedido.setEstado(estado);
        pedidoRepository.save(pedido);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pedido> obtenerPedidosEntre(LocalDate inicio, LocalDate fin) {
        if (inicio == null || fin == null) return obtenerPedidos();
        if (fin.isBefore(inicio)) throw new IllegalArgumentException("La fecha final debe ser posterior a la inicial.");
        return pedidoRepository.findByFechaBetweenOrderByFechaDesc(inicio.atStartOfDay(), fin.atTime(LocalTime.MAX));
    }

    @Override
    @Transactional(readOnly = true)
    public Pedido obtenerPedidoCliente(Integer idPedido, String username) {
        return pedidoRepository.findComprobante(idPedido, username).orElse(null);
    }

    private void validarProducto(Producto producto, Integer cantidad) {
        if (!Boolean.TRUE.equals(producto.getActivo()) || producto.getPrecio() == null
                || producto.getExistencias() == null || cantidad == null || cantidad < 1
                || producto.getExistencias() < cantidad) {
            throw new IllegalStateException("Uno de los productos ya no cuenta con existencias suficientes. Actualiza tu carrito.");
        }
    }
}
