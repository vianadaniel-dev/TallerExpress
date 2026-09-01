# AGENTS.md

## Propósito del proyecto
Este repositorio contiene una aplicación de escritorio Java para gestionar un taller automotriz. El sistema permite:

- registrar y autenticar usuarios
- gestionar clientes y vehículos
- administrar inventario de repuestos
- crear órdenes de servicio
- consultar historial por vehículo
- calcular costo total de reparación

La aplicación usa arquitectura por capas con enfoque MVC simplificado:

- model: entidades del dominio
- repository: acceso a PostgreSQL mediante JDBC
- service: reglas de negocio
- decorator: logging y valores por defecto
- controller: orquestación entre vistas y servicios
- view: interfaz Swing para uso del usuario

---

## Punto de entrada
Archivo principal:

- src/main/java/com/mycompany/tallerexpress/TallerExpress.java

Función principal:

- `main(String[] args)`

Flujo de arranque:

1. Crea instancias de repositorios:
   - `UsuarioRepositoryImpl`
   - `RepuestoRepositoryImpl`
   - `OrdenServicioRepositoryImpl`
2. Envuelve servicios con decoradores:
   - `HttpLoggerDecorator`
   - `UsuarioDefaultValuesDecorator`
   - `RepuestoHttpLoggerDecorator`
3. Crea los controladores:
   - `UsuarioController`
   - `RepuestoController`
   - `OrdenServicioController`
4. Abre `LoginView`
5. Si login correcto, abre `OrdenServicioView`

---

## Configuración y entorno

### Base de datos
Archivo:

- docker-compose.yml

El proyecto usa PostgreSQL en Docker:

- servicio: `db`
- imagen: `postgres:16-alpine`
- usuario: `postgres`
- contraseña: `123456`
- puerto: `5432`

Conexión JDBC:

- src/main/java/com/mycompany/tallerexpress/config/DataBaseConnection.java

URL actual:

- `jdbc:postgresql://localhost:5432/TallerExpress`

Credenciales hardcodeadas actuales:

- usuario: `postgres`
- password: `123456`

> Nota: en un proyecto más robusto conviene mover estas credenciales a variables de entorno.

### Maven
Archivo:

- pom.xml

Dependencias clave:

- PostgreSQL JDBC driver
- Java 21 (`maven.compiler.release=21`)

Clase principal:

- `com.mycompany.tallerexpress.TallerExpress`

---

## Modelos principales

### Usuario
Archivo:

- src/main/java/com/mycompany/tallerexpress/model/Usuario.java

Atributos:

- `id`
- `username`
- `password`
- `role` (`Role`)
- `estado`
- `createdAt`

Métodos relevantes:

- `getId`, `setId`
- `getUsername`, `setUsername`
- `getPassword`, `setPassword`
- `getRole`, `setRole`
- `getEstado`, `setEstado`
- `getCreatedAt`, `setCreatedAt`

### Role
Archivo:

- src/main/java/com/mycompany/tallerexpress/model/Role.java

Valores:

- `ADMIN`
- `RECEPCIONISTA`

### Cliente
Archivo:

- src/main/java/com/mycompany/tallerexpress/model/Cliente.java

Atributos:

- `id`
- `nombre`
- `email`
- `vehiculos: List<ClienteVehiculo>`

### ClienteVehiculo
Archivo:

- src/main/java/com/mycompany/tallerexpress/model/ClienteVehiculo.java

Atributos:

- `id`
- `placa`
- `clienteId`

### Repuesto
Archivo:

- src/main/java/com/mycompany/tallerexpress/model/Repuesto.java

Atributos:

- `id`
- `codigoReferencia`
- `nombre`
- `categoria`
- `proveedor`
- `stockTotal`
- `stockDisponible`
- `precioUnitario`
- `Activo`
- `created`

Métodos relevantes:

- `setStockTotal(int)`
- `setStockDisponible(int)`
- `setPrecioUnitario(double)`
- `isActivo`, `setActivo`
- `createdAt()`
- `mostrarInformacion()`

### OrdenServicio
Archivo:

- src/main/java/com/mycompany/tallerexpress/model/OrdenServicio.java

Atributos:

- `id`
- `clienteId`
- `vehiculoId`
- `mecanico`
- `fechaIngreso`
- `descripcionProblema`
- `diagnostico`
- `estado`
- `repuestosUtilizados: List<DetalleRepuestoOrden>`

Métodos relevantes:

- `calcularCostoTotal()`
- `getRepuestosUtilizados`, `setRepuestosUtilizados`

### DetalleRepuestoOrden
Archivo:

- src/main/java/com/mycompany/tallerexpress/model/DetalleRepuestoOrden.java

Atributos:

- `repuesto`
- `cantidad`
- `precioUnitario`

Método relevante:

- `getSubtotal()`

---

## Repositorios y persistencia

Todos los repositorios usan JDBC y conectan a PostgreSQL mediante `DataBaseConnection.getConnection()`.

### UsuarioRepository
Archivo:

- src/main/java/com/mycompany/tallerexpress/repository/UsuarioRepository.java

Métodos:

- `guardar(Usuario usuario)`
- `autenticar(String username, String password)`

Implementación:

- `UsuarioRepositoryImpl`

SQL clave:

- insertar usuario en `usuarios`
- buscar por `username`, `password` y `estado = 'ACTIVO'`

### RepuestoRepository
Archivo:

- src/main/java/com/mycompany/tallerexpress/repository/RepuestoRepository.java

Métodos:

- `guardar(Repuesto repuesto)`
- `actualizar(Repuesto repuesto)`
- `listar()`
- `listarPorCategoria(String categoria)`
- `listarPorProveedor(String proveedor)`

Implementación:

- `RepuestoRepositoryImpl`

SQL clave:

- insertar en `repuestos`
- actualizar `repuestos`
- consultar por categoria/proveedor

### OrdenServicioRepository
Archivo:

- src/main/java/com/mycompany/tallerexpress/repository/OrdenServicioRepository.java

Métodos:

- `guardar(OrdenServicio orden)`
- `actualizarEstado(int ordenId, String nuevoEstado, String diagnostico)`
- `consultarHistorialPorVehiculo(int vehiculoId)`
- `buscarPorId(int ordenId)`

Implementación:

- `OrdenServicioRepositoryImpl`

Consideraciones:

- usa transacción con `conn.setAutoCommit(false)`
- inserta la orden en `ordenes_servicio`
- inserta repuestos en `orden_repuestos`
- hace commit al final

---

## Servicios y reglas de negocio

### UsuarioService
Archivo:

- src/main/java/com/mycompany/tallerexpress/service/UsuarioService.java

Métodos:

- `create(Usuario usuario)`
- `login(String username, String password)`

Implementación:

- `UsuarioServiceImpl`

Lógica:

- delega creación y autenticación al repositorio

### RepuestoService
Archivo:

- src/main/java/com/mycompany/tallerexpress/service/RepuestoService.java

Métodos:

- `guardar(Repuesto repuesto)`
- `actualizar(Repuesto repuesto)`
- `listar()`
- `listarPorCategoria(String categoria)`
- `listarPorProveedor(String proveedor)`

Implementación:

- `RepuestoServiceImpl`

Validaciones:

- stock no puede ser negativo
- `stockTotal` y `stockDisponible` deben ser coherentes

### OrdenServicioService
Archivo:

- src/main/java/com/mycompany/tallerexpress/service/OrdenServicioService.java

Métodos:

- `crearOrden(OrdenServicio orden)`
- `actualizarEstado(int ordenId, String nuevoEstado, String diagnostico)`
- `consultarHistorialVehiculo(int vehiculoId)`
- `calcularCostoTotal(int ordenId)`

Implementación:

- `OrdenServicioServiceImpl`

Validaciones:

- cliente y vehículo obligatorios
- mecánico obligatorio
- si no hay fecha, se asigna `new Date()`
- si no hay estado, se setea `PENDIENTE`

---

## Controladores

### UsuarioController
Archivo:

- src/main/java/com/mycompany/tallerexpress/config/controller/UsuarioController.java

Métodos:

- `login(String username, String password)`
- `registrarUsuario(String username, String password)`

Valida:

- usuario y contraseña no vacíos

### RepuestoController
Archivo:

- src/main/java/com/mycompany/tallerexpress/config/controller/RepuestoController.java

Métodos:

- `guardar(Repuesto repuesto)`
- `actualizar(Repuesto repuesto)`
- `listar()`
- `listarPorCategoria(String categoria)`
- `listarPorProveedor(String proveedor)`

### OrdenServicioController
Archivo:

- src/main/java/com/mycompany/tallerexpress/config/controller/OrdenServicioController.java

Métodos:

- `crearOrden(OrdenServicio orden)`
- `actualizarEstado(int ordenId, String nuevoEstado, String diagnostico)`
- `consultarHistorial(int vehiculoId)`
- `obtenerCostoTotal(int ordenId)`

---

## Vistas y flujo UI

### LoginView
Archivo:

- src/main/java/com/mycompany/tallerexpress/LoginView.java

Responsabilidad:

- mostrar diálogo de login con Swing
- pedir usuario y contraseña
- llamar a `UsuarioController.login()`
- controlar máximo de 3 intentos

### OrdenServicioView
Archivo:

- src/main/java/com/mycompany/tallerexpress/view/OrdenServicioView.java

Responsabilidad:

- mostrar menú de órdenes
- registrar orden nueva
- agregar repuestos a la orden
- consultar costo total por orden

Flujos implementados:

1. `mostrarMenuOrdenes()`
2. `registrarOrdenFlujo()`
3. `consultarCostoFlujo()`

---

## Decoradores

### HttpLoggerDecorator
Archivo:

- src/main/java/com/mycompany/tallerexpress/decorator/HttpLoggerDecorator.java

Función:

- loguea llamadas a servicios de usuario en consola con estilo HTTP
- imprime mensajes tipo `POST /api/login`

### UsuarioDefaultValuesDecorator
Archivo:

- src/main/java/com/mycompany/tallerexpress/decorator/UsuarioDefaultValuesDecorator.java

Función:

- asigna valores por defecto al crear un usuario:
  - `role = RECEPCIONISTA`
  - `estado = ACTIVO`
  - `createdAt = new Date()`

### RepuestoHttpLoggerDecorator
Archivo:

- src/main/java/com/mycompany/tallerexpress/RepuestoHttpLoggerDecorator.java

Función:

- decorador concreto para logging de operaciones de repuestos
- se usa en `TallerExpress.main()`

---

## Excepciones del dominio
Ubicación:

- src/main/java/com/mycompany/tallerexpress/exceptions/

Excepciones clave:

- `ClienteActivoException`
- `CodigoRepuestoUnicoException`
- `OrdenDeServicioValidaException`
- `PlacaUnicaException`
- `StockMayorIgualCeroException`
- `VehiculoRegistradoException`

Usos típicos:

- validar código único de repuesto
- validar stock válido y no negativo
- controlar vehículo duplicado
- controlar estado de cliente activo/inactivo

---

## Estructura de tablas esperadas por la app
Se documentan en README, pero el sistema parece asumir estas tablas:

- `usuarios`
  - id
  - username
  - password
  - role
  - estado
  - created_at

- `clientes`
  - id
  - nombre
  - email

- `vehiculos`
  - id
  - placa
  - cliente_id
  - created

- `repuestos`
  - id
  - codigo_referencia
  - nombre
  - categoria
  - proveedor
  - stock_total
  - stock_disponible
  - precio_unitario
  - activo
  - created

- `ordenes_servicio`
  - id
  - cliente_id
  - vehiculo_id
  - mecanico
  - fecha_ingreso
  - descripcion_problema
  - diagnostico
  - estado

- `orden_repuestos`
  - orden_id
  - repuesto_id
  - cantidad
  - precio_unitario

---

## Notas importantes para otros agentes

### 1) Flujo principal de trabajo
La app no es una API REST; es una aplicación Swing con acceso a PostgreSQL directo. La lógica central se ejecuta así:

- `TallerExpress.main()`
- `LoginView.mostrarPantallaLogin()`
- `UsuarioController.login()`
- `UsuarioService.login()`
- `UsuarioRepositoryImpl.autenticar()`

### 2) Patrón de capas seguido por el proyecto
Se intenta mantener una separación clara:

- `model` = entidades
- `repository` = consultas SQL
- `service` = validaciones/negocio
- `controller` = entrada desde UI
- `view` = pantalla Swing

### 3) Decorator usado para logging y defaults
El patrón de decoradores está presente en usuarios, y el flujo en `TallerExpress.main()` confirma cómo se ensamblan:

- `new HttpLoggerDecorator(new UsuarioDefaultValuesDecorator(new UsuarioServiceImpl(usuarioRepo)))`

### 4) Riesgos / bugs detectados
Estos detalles son importantes al editar o extender el proyecto:

- En `Repuesto.getStockTotal()`, el getter retorna `stockDisponible` en vez de `stockTotal`.
- En `OrdenServicioRepositoryImpl.buscarPorId()`, el método devuelve `null` sin implementación.
- `DataBaseConnection` tiene credenciales hardcodeadas.
- `Repuesto.setStockDisponible()` depende de `this.stockTotal`, que puede quedar en 0 si no se ha asignado antes.
- El proyecto usa `javax.swing` y no está preparado para ejecución headless; requiere una estación con UI gráfica.

### 5) Credenciales de acceso por defecto
El README indica usuarios de ejemplo:

- usuario: `admin`
- password: `admin123`
- role: `ADMIN`

- usuario: `recepcion`
- password: `12345`
- role: `RECEPCIONISTA`

---

## Recomendación para próximos agentes
Antes de tocar persistencia o reglas de negocio, revisar primero estos archivos clave:

- `TallerExpress.java`
- `DataBaseConnection.java`
- `UsuarioRepositoryImpl.java`
- `RepuestoRepositoryImpl.java`
- `OrdenServicioRepositoryImpl.java`
- `UsuarioServiceImpl.java`
- `RepuestoServiceImpl.java`
- `OrdenServicioServiceImpl.java`
- `Usuario.java`
- `Repuesto.java`
- `OrdenServicio.java`

Estos archivos representan el núcleo del sistema y permiten entender la aplicación sin leer cada detalle del repo completo.
