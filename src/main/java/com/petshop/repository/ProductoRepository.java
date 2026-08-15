package com.petshop.repository;

import com.petshop.domain.Producto;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    List<Producto> findByActivoTrueOrderByDescripcionAsc();

    Optional<Producto> findByIdProductoAndActivoTrue(Integer idProducto);
}
