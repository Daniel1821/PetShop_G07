package com.petshop.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "resena", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"id_producto", "id_usuario"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Resena {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_resena")
    private Integer idResena;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(name = "puntuacion", nullable = false)
    private Integer puntuacion;

    @Column(name = "comentario", length = 500)
    private String comentario;

    @Column(name = "fecha_creacion", insertable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_modificacion", insertable = false, updatable = false)
    private LocalDateTime fechaModificacion;
}
