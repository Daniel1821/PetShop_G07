package com.petshop.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "tipo_mascota")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoMascota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_mascota")
    private Integer idTipoMascota;

    @Column(name = "descripcion", nullable = false, unique = true, length = 30)
    private String descripcion;

    @Column(name = "activo")
    private Boolean activo;

    @Column(name = "fecha_creacion", insertable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_modificacion", insertable = false, updatable = false)
    private LocalDateTime fechaModificacion;
}
