package com.petshop.repository;

import com.petshop.domain.Producto;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    List<Producto> findByActivoTrueOrderByDescripcionAsc();

    Optional<Producto> findByIdProductoAndActivoTrue(Integer idProducto);

    @Query("select p from Producto p join p.categoria c where p.activo = true and c.activo = true and (:busqueda is null or lower(p.descripcion) like lower(concat('%', :busqueda, '%'))) and (:idCategoria is null or c.idCategoria = :idCategoria) order by p.descripcion")
    List<Producto> buscarProductosActivos(@Param("busqueda") String busqueda, @Param("idCategoria") Integer idCategoria);
}
