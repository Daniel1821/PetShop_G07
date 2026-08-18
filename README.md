# PetShop G07

Aplicación web transaccional para la gestión de una tienda de mascotas. Permite administrar productos, categorías, usuarios, inventario, carrito, direcciones y pedidos.

## Tecnologías

- Java 21 y Spring Boot
- Spring MVC, Spring Security y Thymeleaf
- Hibernate/JPA y MySQL
- Bootstrap 5

## Requisitos

- JDK 21
- MySQL 8 o superior
- Maven o NetBeans

## Base de datos

1. Ejecute el script [creaTablas.sql](src/main/resources/creaTablas.sql) en MySQL.
2. Verifique que el servicio de MySQL esté iniciado en el puerto `3306`.
3. La aplicación usa la base `petshop` y el usuario de desarrollo definido en `application.properties`.

## Ejecución

Abra el proyecto en NetBeans y seleccione **Clean and Build**. Luego ejecute el proyecto. La aplicación queda disponible en:

`http://localhost:91/`

## Usuarios de prueba

| Rol | Usuario | Contraseña |
|---|---|---|
| Administrador | `daniel` | `123` |
| Vendedor | `carlos` | `456` |
| Cliente | `juan` | `789` |

## Módulos principales

- Catálogo público con búsqueda, filtro por categoría e internacionalización ES/EN.
- Administración de productos, categorías y usuarios.
- Inventario con alerta de stock bajo.
- Registro de clientes y recuperación local de contraseña.
- Carrito de compras con validación de existencias.
- Direcciones de entrega administradas por cada cliente.
- Compra transaccional: crea pedido, detalle, descuenta inventario y vacía carrito.
- Historial de pedidos y comprobante de compra.
- Gestión de estados de pedidos para administrador.
- Reporte de ventas por fechas y exportación CSV.

## Funcionalidad investigada

El reporte de ventas permite exportar los resultados a un archivo CSV. Esto facilita analizar las ventas fuera de la aplicación mediante una hoja de cálculo.

## Prueba rápida

1. Inicie sesión como `juan`.
2. Cree o seleccione una dirección en **Mis direcciones**.
3. Agregue productos al carrito y finalice una compra.
4. Consulte **Mis pedidos** y el comprobante.
5. Inicie sesión como `daniel` para gestionar pedidos y revisar `http://localhost:91/reportes/ventas`.

## Evidencias de roles

Las siguientes capturas muestran el acceso diferenciado de cada usuario de prueba:

| Rol | Evidencia |
|---|---|
| Administrador | ![Panel de Daniel](docs/evidencias/1.%20admin%20%28Daniel%29.png) |
| Cliente | ![Panel de Juan](docs/evidencias/2.%20cliente%20%28juan%29.png) |
| Vendedor | ![Panel de Carlos](docs/evidencias/3.%20vendedor%20%28carlos%29.png) |
