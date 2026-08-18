package com.petshop.repository;

import com.petshop.domain.Direccion;
import java.util.Optional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DireccionRepository extends JpaRepository<Direccion, Integer> {

    Optional<Direccion> findFirstByUsuarioUsernameAndPredeterminadaTrue(String username);
    List<Direccion> findByUsuarioUsernameOrderByPredeterminadaDescIdDireccionDesc(String username);
    Optional<Direccion> findByIdDireccionAndUsuarioUsername(Integer idDireccion, String username);
    boolean existsByUsuarioUsernameAndPredeterminadaTrue(String username);
}
