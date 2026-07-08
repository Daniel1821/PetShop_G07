
package com.petshop.domain;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Llave primaria compuesta para la tabla usuario_rol (id_usuario + id_rol).
 */
@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRolId implements Serializable {

    private Integer idUsuario;
    private Integer idRol;
}