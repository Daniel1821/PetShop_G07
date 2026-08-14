package com.petshop.repository;

import com.petshop.domain.UsuarioRol;
import com.petshop.domain.UsuarioRolId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UsuarioRolRepository extends JpaRepository<UsuarioRol, UsuarioRolId> {

    @Query("select ur from UsuarioRol ur join fetch ur.rol where ur.usuario.username = :username")
    List<UsuarioRol> findRolesByUsername(@Param("username") String username);
}
