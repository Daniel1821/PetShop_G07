package com.petshop.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "direccion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Direccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_direccion")
    private Integer idDireccion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(name = "provincia", nullable = false, length = 30)
    private String provincia;

    @Column(name = "canton", nullable = false, length = 30)
    private String canton;

    @Column(name = "distrito", nullable = false, length = 30)
    private String distrito;

    @Column(name = "senas", nullable = false, length = 255)
    private String senas;

    @Column(name = "telefono_contacto", length = 25)
    private String telefonoContacto;

    @Column(name = "predeterminada")
    private Boolean predeterminada;

    @Column(name = "fecha_creacion", insertable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_modificacion", insertable = false, updatable = false)
    private LocalDateTime fechaModificacion;
}
