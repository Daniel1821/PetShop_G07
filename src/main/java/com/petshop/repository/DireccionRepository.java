package com.petshop.repository;

import com.petshop.domain.Direccion;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DireccionRepository extends JpaRepository<Direccion, Integer> {

    Optional<Direccion> findFirstByUsuarioUsernameAndPredeterminadaTrue(String username);
}
