package com.petshop.repository;

import com.petshop.domain.DetallePedido;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Integer> {

    @Query("select d.producto.descripcion as descripcion, sum(d.cantidad) as cantidadVendida from DetallePedido d group by d.producto.descripcion order by sum(d.cantidad) desc")
    List<ProductoVendido> findProductosMasVendidos();
}
