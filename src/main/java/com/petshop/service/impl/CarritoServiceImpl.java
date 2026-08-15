package com.petshop.service.impl;

import com.petshop.domain.CarritoDetalle;
import com.petshop.domain.Producto;
import com.petshop.domain.Usuario;
import com.petshop.repository.CarritoDetalleRepository;
import com.petshop.repository.ProductoRepository;
import com.petshop.repository.UsuarioRepository;
import com.petshop.service.CarritoService;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CarritoServiceImpl implements CarritoService {
    private final CarritoDetalleRepository carritoDetalleRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;

    public CarritoServiceImpl(CarritoDetalleRepository carritoDetalleRepository, ProductoRepository productoRepository, UsuarioRepository usuarioRepository) {
        this.carritoDetalleRepository = carritoDetalleRepository;
        this.productoRepository = productoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override @Transactional(readOnly = true)
    public List<CarritoDetalle> obtenerCarrito(String username) { return carritoDetalleRepository.findCarritoByUsername(username); }

    @Override @Transactional
    public void agregarProducto(String username, Integer idProducto, Integer cantidad) {
        validarCantidad(cantidad);
        Producto producto = productoRepository.findByIdProductoAndActivoTrue(idProducto).orElseThrow(() -> new IllegalArgumentException("El producto ya no está disponible."));
        Usuario usuario = usuarioRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado."));
        CarritoDetalle detalle = carritoDetalleRepository.findByUsuarioUsernameAndProductoIdProducto(username, idProducto).orElseGet(() -> nuevoDetalle(usuario, producto));
        int cantidadFinal = detalle.getCantidad() + cantidad;
        validarExistencias(producto, cantidadFinal);
        detalle.setCantidad(cantidadFinal);
        carritoDetalleRepository.save(detalle);
    }

    @Override @Transactional
    public void actualizarCantidad(String username, Integer idCarritoDetalle, Integer cantidad) {
        validarCantidad(cantidad);
        CarritoDetalle detalle = buscarDetalleUsuario(username, idCarritoDetalle);
        validarExistencias(detalle.getProducto(), cantidad);
        detalle.setCantidad(cantidad);
        carritoDetalleRepository.save(detalle);
    }

    @Override @Transactional
    public void eliminarProducto(String username, Integer idCarritoDetalle) { carritoDetalleRepository.delete(buscarDetalleUsuario(username, idCarritoDetalle)); }

    private CarritoDetalle nuevoDetalle(Usuario usuario, Producto producto) { CarritoDetalle detalle = new CarritoDetalle(); detalle.setUsuario(usuario); detalle.setProducto(producto); detalle.setCantidad(0); return detalle; }
    private CarritoDetalle buscarDetalleUsuario(String username, Integer id) { return carritoDetalleRepository.findByIdCarritoDetalleAndUsuarioUsername(id, username).orElseThrow(() -> new IllegalArgumentException("El producto no pertenece a tu carrito.")); }
    private void validarCantidad(Integer cantidad) { if (cantidad == null || cantidad < 1) throw new IllegalArgumentException("La cantidad debe ser al menos 1."); }
    private void validarExistencias(Producto producto, int cantidad) { if (producto.getExistencias() == null || producto.getExistencias() < cantidad) throw new IllegalArgumentException("No hay existencias suficientes para la cantidad solicitada."); }
}
