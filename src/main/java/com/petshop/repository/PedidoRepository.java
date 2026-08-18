package com.petshop.repository;

import com.petshop.domain.Pedido;
import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {

    List<Pedido> findByUsuarioUsernameOrderByFechaDesc(String username);

    @Query("select p from Pedido p join fetch p.usuario order by p.fecha desc")
    List<Pedido> findAllWithUsuarioOrderByFechaDesc();
    @Query("select p from Pedido p join fetch p.usuario where p.fecha between :inicio and :fin order by p.fecha desc")
    List<Pedido> findByFechaBetweenOrderByFechaDesc(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);
    @Query("select distinct p from Pedido p join fetch p.usuario join fetch p.direccion left join fetch p.detalles d left join fetch d.producto where p.idPedido = :id and p.usuario.username = :username")
    Optional<Pedido> findComprobante(@Param("id") Integer id, @Param("username") String username);
}
