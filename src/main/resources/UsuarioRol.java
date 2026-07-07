package com.petshop.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer idUsuario;

    @Column(name = "username", nullable = false, unique = true, length = 30)
    private String username;

    @Column(name = "password", nullable = false, length = 512)
    private String password;

    @Column(name = "nombre", nullable = false, length = 20)
    private String nombre;

    @Column(name = "apellidos", nullable = false, length = 30)
    private String apellidos;

    @Column(name = "correo", unique = true, length = 75)
    private String correo;

    @Column(name = "telefono", length = 25)
    private String telefono;

    @Column(name = "ruta_imagen", length = 1024)
    private String rutaImagen;

    @Column(name = "activo")
    private Boolean activo;

    @Column(name = "fecha_creacion", insertable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_modificacion", insertable = false, updatable = false)
    private LocalDateTime fechaModificacion;
}
