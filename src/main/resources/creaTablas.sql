/*
  Script de creación de base de datos para PetShop
  Tienda de objetos divergente para mascotas
  Este script crea el esquema, tablas, usuarios, y
  carga datos de ejemplo.

  Basado en la estructura y convenciones del script de techshop.
*/
-- Sección de administración (ejecutar una vez en un entorno de desarrollo)
drop database if exists petshop;
drop user if exists usuario_prueba;
drop user if exists usuario_reportes;

-- Creación del esquema
CREATE database petshop
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;

-- Creación de usuarios con contraseñas seguras (idealmente asignadas fuera del script)
create user 'usuario_prueba'@'%' identified by 'Usuar1o_Clave.';
create user 'usuario_reportes'@'%' identified by 'Usuar1o_Reportes.';

-- Asignación de permisos
-- Se otorgan permisos específicos en lugar de todos los permisos a todas las tablas futuras
grant select, insert, update, delete on petshop.* to 'usuario_prueba'@'%';
grant select on petshop.* to 'usuario_reportes'@'%';
flush privileges;

use petshop;

-- --- Sección de Creación de Tablas ---

-- Tabla de roles
create table rol (
  id_rol INT NOT NULL AUTO_INCREMENT,
  rol varchar(20) unique,
  fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  primary key (id_rol))
  ENGINE = InnoDB;

-- Tabla de usuarios
CREATE TABLE usuario (
  id_usuario INT NOT NULL AUTO_INCREMENT,
  username varchar(30) NOT NULL UNIQUE,
  password varchar(512) NOT NULL,
  nombre VARCHAR(20) NOT NULL,
  apellidos VARCHAR(30) NOT NULL,
  correo VARCHAR(75) NULL UNIQUE,
  telefono VARCHAR(25) NULL,
  ruta_imagen varchar(1024),
  activo boolean,
  fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_usuario`),
  CHECK (correo REGEXP '^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$'),
  index ndx_username (username))
  ENGINE = InnoDB;

-- Tabla de relación entre usuarios y roles
create table usuario_rol (
  id_usuario int not null,
  id_rol INT NOT NULL,
  fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id_usuario,id_rol),
  foreign key fk_usuarioRol_usuario (id_usuario) references usuario(id_usuario),
  foreign key fk_usuarioRol_rol (id_rol) references rol(id_rol))
  ENGINE = InnoDB;

-- Tabla de direcciones de entrega (HU-19)
create table direccion (
  id_direccion INT NOT NULL AUTO_INCREMENT,
  id_usuario INT NOT NULL,
  provincia VARCHAR(30) NOT NULL,
  canton VARCHAR(30) NOT NULL,
  distrito VARCHAR(30) NOT NULL,
  senas VARCHAR(255) NOT NULL,
  telefono_contacto VARCHAR(25),
  predeterminada boolean DEFAULT FALSE,
  fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id_direccion),
  index ndx_direccion_usuario (id_usuario),
  foreign key fk_direccion_usuario (id_usuario) references usuario(id_usuario))
  ENGINE = InnoDB;

-- Tabla de tipos de mascota (HU-08, filtrado del catálogo)
create table tipo_mascota (
  id_tipo_mascota INT NOT NULL AUTO_INCREMENT,
  descripcion VARCHAR(30) NOT NULL,
  activo boolean,
  fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id_tipo_mascota),
  unique (descripcion))
  ENGINE = InnoDB;

-- Tabla de categorías (HU-03, HU-06)
create table categoria (
  id_categoria INT NOT NULL AUTO_INCREMENT,
  descripcion VARCHAR(50) NOT NULL,
  ruta_imagen varchar(1024),
  activo boolean,
  fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id_categoria),
  unique (descripcion),
  index ndx_descripcion (descripcion))
  ENGINE = InnoDB;

-- Tabla de productos
create table producto (
  id_producto INT NOT NULL AUTO_INCREMENT,
  id_categoria INT NOT NULL,
  id_tipo_mascota INT NOT NULL,
  descripcion VARCHAR(50) NOT NULL,
  detalle text,
  precio decimal(12,2) CHECK (precio >= 0),
  existencias int unsigned CHECK (existencias >= 0),
  calificacion decimal(2,1) DEFAULT NULL CHECK (calificacion IS NULL OR (calificacion >= 0 AND calificacion <= 5)),
  ruta_imagen varchar(1024),
  activo boolean,
  fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id_producto),
  unique (descripcion),
  index ndx_descripcion (descripcion),
  foreign key fk_producto_categoria (id_categoria) references categoria(id_categoria),
  foreign key fk_producto_tipoMascota (id_tipo_mascota) references tipo_mascota(id_tipo_mascota))
  ENGINE = InnoDB;

-- Tabla de imágenes de producto (HU-16, permite varias imágenes por producto)
create table producto_imagen (
  id_imagen INT NOT NULL AUTO_INCREMENT,
  id_producto INT NOT NULL,
  ruta_imagen varchar(1024) NOT NULL,
  orden int unsigned DEFAULT 0,
  fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id_imagen),
  index ndx_imagen_producto (id_producto),
  foreign key fk_imagen_producto (id_producto) references producto(id_producto))
  ENGINE = InnoDB;

-- Tabla de carrito de compras (HU-10, HU-11)
create table carrito_detalle (
  id_carrito_detalle INT NOT NULL AUTO_INCREMENT,
  id_usuario INT NOT NULL,
  id_producto INT NOT NULL,
  cantidad int unsigned CHECK (cantidad > 0),
  fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id_carrito_detalle),
  UNIQUE (id_usuario, id_producto),
  index ndx_carrito_usuario (id_usuario),
  index ndx_carrito_producto (id_producto),
  foreign key fk_carrito_usuario (id_usuario) references usuario(id_usuario),
  foreign key fk_carrito_producto (id_producto) references producto(id_producto))
  ENGINE = InnoDB;

-- Tabla de pedidos (HU-12, HU-13, HU-14)
create table pedido (
  id_pedido INT NOT NULL AUTO_INCREMENT,
  id_usuario INT NOT NULL,
  id_direccion INT NOT NULL,
  fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  total decimal(12,2) check (total > 0),
  estado ENUM('Pendiente','Procesando','Enviado','Entregado','Cancelado') NOT NULL DEFAULT 'Pendiente',
  fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id_pedido),
  index ndx_pedido_usuario (id_usuario),
  foreign key fk_pedido_usuario (id_usuario) references usuario(id_usuario),
  foreign key fk_pedido_direccion (id_direccion) references direccion(id_direccion))
  ENGINE = InnoDB;

-- Tabla de detalle de pedido
create table detalle_pedido (
  id_detalle_pedido INT NOT NULL AUTO_INCREMENT,
  id_pedido INT NOT NULL,
  id_producto INT NOT NULL,
  precio_historico decimal(12,2) check (precio_historico >= 0),
  cantidad int unsigned check (cantidad > 0),
  fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id_detalle_pedido),
  index ndx_pedido (id_pedido),
  index ndx_producto (id_producto),
  UNIQUE (id_pedido, id_producto),
  foreign key fk_detalle_pedido (id_pedido) references pedido(id_pedido),
  foreign key fk_detalle_producto (id_producto) references producto(id_producto))
  ENGINE = InnoDB;

-- Tabla de reseñas de producto (baja prioridad, soporta calificación)
create table resena (
  id_resena INT NOT NULL AUTO_INCREMENT,
  id_producto INT NOT NULL,
  id_usuario INT NOT NULL,
  puntuacion tinyint unsigned NOT NULL CHECK (puntuacion >= 1 AND puntuacion <= 5),
  comentario VARCHAR(500),
  fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id_resena),
  UNIQUE (id_producto, id_usuario),
  index ndx_resena_producto (id_producto),
  foreign key fk_resena_producto (id_producto) references producto(id_producto),
  foreign key fk_resena_usuario (id_usuario) references usuario(id_usuario))
  ENGINE = InnoDB;

-- Tabla de rutas
CREATE TABLE ruta (
    id_ruta INT AUTO_INCREMENT NOT NULL,
    ruta VARCHAR(255) NOT NULL,
    id_rol INT NULL,
    requiere_rol boolean NOT NULL DEFAULT TRUE,
  fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    check (id_rol IS NOT NULL OR requiere_rol = FALSE),
    PRIMARY KEY (id_ruta),
    FOREIGN KEY (id_rol) REFERENCES rol(id_rol))
    ENGINE = InnoDB;

-- Tabla de constantes de la aplicación
CREATE TABLE constante (
    id_constante INT AUTO_INCREMENT NOT NULL,
    atributo VARCHAR(25) NOT NULL,
    valor VARCHAR(150) NOT NULL,
  fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id_constante),
    UNIQUE (atributo))
    ENGINE = InnoDB;

-- --- Sección de Inserción de Datos ---

-- Inserción de roles
insert into rol (rol) values ('ADMIN'), ('VENDEDOR'), ('CLIENTE');

-- Inserción de usuarios
INSERT INTO usuario (username,password,nombre, apellidos, correo, telefono,ruta_imagen,activo) VALUES
('asanchez','$2a$10$P1.w58XvnaYQUQgZUCk4aO/RTRl8EValluCqB3S2VMLTbRt.tlre.','Antoine', 'Sánchez Zárate', 'asanchez@petshop.com', '8811-2345', 'https://randomuser.me/api/portraits/men/11.jpg', true),
('dberrocal','$2a$10$GkEj.ZzmQa/aEfDmtLIh3udIH5fMphx/35d0EYeqZL5uzgCJ0lQRi','Daniel', 'Berrocal Sánchez', 'dberrocal@petshop.com', '8822-3456', 'https://randomuser.me/api/portraits/men/22.jpg', true),
('mrojas','$2a$10$koGR7eS22Pv5KdaVJKDcge04ZB53iMiw76.UjHPY.XyVYlYqXnPbO','María', 'Rojas Vargas', 'mrojas@gmail.com', '8833-4567', 'https://randomuser.me/api/portraits/women/33.jpg', true);

-- Asignación de roles a usuarios
insert into usuario_rol (id_usuario, id_rol) values
 (1,1), (1,2), (1,3), (2,2), (2,3), (3,3);

-- Inserción de direcciones
INSERT INTO direccion (id_usuario,provincia,canton,distrito,senas,telefono_contacto,predeterminada) VALUES
(1,'San José','Central','Carmen','100m norte del parque central','8811-2345',true),
(2,'Alajuela','Central','Alajuela','Del hospital 200m oeste','8822-3456',true),
(3,'Heredia','Central','Heredia','Frente a la universidad','8833-4567',true);

-- Inserción de tipos de mascota
INSERT INTO tipo_mascota (descripcion,activo) VALUES
('Perro',true), ('Gato',true), ('Ave',true), ('Pez',true), ('Reptil',true), ('Conejo',true);

-- Inserción de categorias
INSERT INTO categoria (descripcion,ruta_imagen,activo) VALUES
('Alimentos', 'https://images.unsplash.com/photo-1585846888147-303d39253226', true),
('Juguetes',  'https://images.unsplash.com/photo-1601758228041-3caa4b7b2fd8', true),
('Accesorios','https://images.unsplash.com/photo-1601758124096-1f7d78606df9', true),
('Higiene y cuidado','https://images.unsplash.com/photo-1601758064978-58c5a3b5a3b6', true),
('Camas y descanso','https://images.unsplash.com/photo-1601758123927-196d9df03b7a', false);

-- Inserción de productos
INSERT INTO producto (id_categoria,id_tipo_mascota,descripcion,detalle,precio,existencias,calificacion,ruta_imagen,activo) VALUES
(1,1,'Croquetas Premium Perro Adulto','Alimento balanceado para perros adultos de todas las razas, formulado con proteína de alta calidad.',18500,25,4.5,'https://images.unsplash.com/photo-1568640347023-a616a30bc3bd',true),
(1,2,'Alimento Gato Esterilizado','Croquetas especiales para gatos esterilizados, control de peso y bola de pelo.',16200,30,4.2,'https://images.unsplash.com/photo-1592194996308-7b43878e84a6',true),
(1,4,'Alimento en Escamas para Peces','Alimento balanceado en escamas para peces tropicales de agua dulce.',3200,50,4.0,'https://images.unsplash.com/photo-1524704796725-9fc3044a58b2',true),
(2,1,'Pelota de Goma Resistente','Juguete de goma resistente para masticar, ideal para perros medianos y grandes.',5400,40,4.7,'https://images.unsplash.com/photo-1591946614720-90a587da4a36',true),
(2,2,'Ratón de Peluche con Cascabel','Juguete interactivo para gatos con relleno de catnip y cascabel.',3100,35,4.3,'https://images.unsplash.com/photo-1592194996308-7b43878e84a6',true),
(3,1,'Correa Retráctil 5m','Correa retráctil resistente hasta 25kg, con freno de seguridad.',12800,20,4.6,'https://images.unsplash.com/photo-1601758228041-3caa4b7b2fd8',true),
(3,3,'Jaula para Ave Mediana','Jaula de metal con comederos y percha incluida para aves medianas.',34500,10,4.1,'https://images.unsplash.com/photo-1552728089-57bdde30beb3',true),
(3,5,'Terrario de Vidrio 40L','Terrario de vidrio con tapa ventilada, ideal para reptiles pequeños.',42000,8,4.4,'https://images.unsplash.com/photo-1585110396000-c9ffd4e6b308',true),
(4,1,'Shampoo Antipulgas Perro','Shampoo medicado antipulgas y garrapatas para perros.',7900,22,4.3,'https://images.unsplash.com/photo-1601758064978-58c5a3b5a3b6',true),
(4,6,'Cepillo para Conejo','Cepillo suave especial para el cuidado del pelaje de conejos.',4500,15,4.0,'https://images.unsplash.com/photo-1585846888147-303d39253226',true);

-- Inserción de imágenes de producto adicionales
INSERT INTO producto_imagen (id_producto,ruta_imagen,orden) VALUES
(1,'https://images.unsplash.com/photo-1568640347023-a616a30bc3bd',1),
(1,'https://images.unsplash.com/photo-1568640347023-a616a30bc3be',2),
(4,'https://images.unsplash.com/photo-1591946614720-90a587da4a36',1),
(6,'https://images.unsplash.com/photo-1601758228041-3caa4b7b2fd8',1);

-- Inserción de carrito de compras
INSERT INTO carrito_detalle (id_usuario,id_producto,cantidad) VALUES
(3,1,2),
(3,6,1),
(2,4,1);

-- Inserción de pedidos
INSERT INTO pedido (id_usuario,id_direccion,fecha,total,estado) VALUES
(3,3,'2025-06-05',40300,'Entregado'),
(2,2,'2025-06-10',54100,'Enviado'),
(3,3,'2025-06-20',16200,'Pendiente');

-- Inserción de detalle de pedidos
INSERT INTO detalle_pedido (id_pedido,id_producto,precio_historico,cantidad) VALUES
(1,1,18500,2),
(1,3,3200,1),
(2,7,34500,1),
(2,9,7900,2),
(3,2,16200,1);

-- Inserción de reseñas
INSERT INTO resena (id_producto,id_usuario,puntuacion,comentario) VALUES
(1,3,5,'Excelente calidad, a mi perro le encanta.'),
(4,2,4,'Muy resistente, dura bastante.'),
(7,3,4,'Buen tamaño y fácil de limpiar.');

-- Inserción de rutas con roles específicos
INSERT INTO ruta (ruta, id_rol) VALUES
('/producto/nuevo', 1),
('/producto/guardar', 1),
('/producto/modificar/**', 1),
('/producto/eliminar/**', 1),
('/categoria/nuevo', 1),
('/categoria/guardar', 1),
('/categoria/modificar/**', 1),
('/categoria/eliminar/**', 1),
('/usuario/**', 1),
('/constante/**', 1),
('/rol/**', 1),
('/usuario_rol/**', 1),
('/ruta/**', 1),
('/producto/listado', 2),
('/categoria/listado', 2),
('/inventario/**', 2),
('/pedido/gestion/**', 2),
('/reportes/**', 2),
('/carrito/**', 3),
('/pedido/carrito', 3),
('/pedido/historial', 3),
('/direccion/**', 3),
('/resena/**', 3);

-- Inserción de rutas que no requieren rol
INSERT INTO ruta (ruta,requiere_rol) VALUES
('/',false),
('/index',false),
('/catalogo/**',false),
('/errores/**',false),
('/registro/**',false),
('/recuperar-contrasena/**',false),
('/403',false),
('/fav/**',false),
('/js/**',false),
('/css/**',false),
('/webjars/**',false);

-- Inserción de constantes de la aplicación
INSERT INTO constante (atributo,valor) VALUES
('dominio','localhost'),
('dolar','520.75'),
('servidor.http','http://localhost'),
('stock_minimo_default','5');
