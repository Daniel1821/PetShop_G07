package com.petshop.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "producto")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Integer idProducto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_categoria", nullable = false)
    private Categoria categoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tipo_mascota", nullable = false)
    private TipoMascota tipoMascota;

    @Column(name = "descripcion", nullable = false, unique = true, length = 50)
    private String descripcion;

    @Lob
    @Column(name = "detalle")
    private String detalle;

    @Column(name = "precio", precision = 12, scale = 2)
    private BigDecimal precio;

    @Column(name = "existencias")
    private Integer existencias;

    @Column(name = "calificacion", precision = 2, scale = 1)
    private BigDecimal calificacion;

    @Column(name = "ruta_imagen", length = 1024)
    private String rutaImagen;

    @Column(name = "activo")
    private Boolean activo;

    @Column(name = "fecha_creacion", insertable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_modificacion", insertable = false, updatable = false)
    private LocalDateTime fechaModificacion;

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductoImagen> imagenes = new ArrayList<>();
}
