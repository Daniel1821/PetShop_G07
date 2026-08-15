package com.petshop.repository;

import com.petshop.domain.CarritoDetalle;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CarritoDetalleRepository extends JpaRepository<CarritoDetalle, Integer> {

    @Query("select cd from CarritoDetalle cd join fetch cd.producto where cd.usuario.username = :username order by cd.fechaCreacion desc")
    List<CarritoDetalle> findCarritoByUsername(@Param("username") String username);

    Optional<CarritoDetalle> findByUsuarioUsernameAndProductoIdProducto(String username, Integer idProducto);

    Optional<CarritoDetalle> findByIdCarritoDetalleAndUsuarioUsername(Integer idCarritoDetalle, String username);
}
