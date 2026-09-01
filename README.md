# TallerExpress
Aplicacion de escritorio construida en JAVA SE con el fin de facilitar la gestion eficiente del inventario de repuestos, manejo de clientes, vehiculos y mantener su organizacion mediantes un sistema integro por capaz y con interfaz amigable.

Aplicación de escritorio desarrollada en **Java** utilizando:

- ☕ Java
- 🐘 PostgreSQL
- 🔌 JDBC
- 🖥️ `JOptionPane`
- 🏗️ Arquitectura por capas
- 🎯 Patrón MVC simplificado
- 📦 Maven

La aplicación permite registrar, listar, buscar y eliminar clientes, repuestos, citas de taller almacenados en una base de datos PostgreSQL.

## 🛠️ Requisitos

Antes de ejecutar el proyecto, instala:

* ☕ **JDK 17** o superior
* 🐘 **PostgreSQL**
* 🧰 **pgAdmin 4**
* 📦 **Maven**
* 💻 **IntelliJ IDEA**, **Eclipse**, **NetBeans** o **VS Code**

Puedes comprobar las versiones instaladas en tu terminal con:

```bash
java -version
```

## 📦 Configuración del proyecto

Si utilizas Maven, agrega el driver de PostgreSQL en tu archivo `pom.xml`:

```xml
<project xmlns="[http://maven.apache.org/POM/4.0.0](http://maven.apache.org/POM/4.0.0)"
         xmlns:xsi="[http://www.w3.org/2001/XMLSchema-instance](http://www.w3.org/2001/XMLSchema-instance)"
         xsi:schemaLocation="
         [http://maven.apache.org/POM/4.0.0](http://maven.apache.org/POM/4.0.0)
         [https://maven.apache.org/xsd/maven-4.0.0.xsd](https://maven.apache.org/xsd/maven-4.0.0.xsd)">

    <modelVersion>4.0.0</modelVersion>

    <groupId>com.mycompany</groupId>
    <artifactId>TallerExpress</artifactId>
    <version>1.0-SNAPSHOT</version>

    <properties>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
            <version>42.7.6</version>
        </dependency>
    </dependencies>

</project>
```

Inicializacion de Base de Datos en PGAdmin4
```
CREATE TABLE repuestos (
    id SERIAL PRIMARY KEY,
    codigo_referencia INT NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    categoria VARCHAR(50) NOT NULL,
    proveedor VARCHAR(100) NOT NULL,
    stock_total INT NOT NULL DEFAULT 0,
    stock_disponible INT NOT NULL DEFAULT 0,
    precio_unitario DECIMAL(10, 2) NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Restricción para evitar que el stock disponible supere al stock total o sea negativo
    CONSTRAINT check_stock_valido CHECK (stock_disponible >= 0 AND stock_disponible <= stock_total)
);

-- Tabla de Clientes
CREATE TABLE clientes (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE
);

-- Tabla de Vehículos (Relacionada con Clientes)
CREATE TABLE vehiculos (
    id SERIAL PRIMARY KEY,
    placa VARCHAR(10) NOT NULL UNIQUE, -- Valida placa única a nivel de DB
    cliente_id INT NOT NULL,
    created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_cliente FOREIGN KEY (cliente_id) REFERENCES clientes(id) ON DELETE CASCADE
);

CREATE TABLE usuarios (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL, -- 'ADMIN' o 'RECEPCIONISTA'
    estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVO',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE ordenes_servicio (
    id SERIAL PRIMARY KEY,
    cliente_id INT NOT NULL,
    vehiculo_id INT NOT NULL,
    mecanico VARCHAR(100) NOT NULL,
    fecha_ingreso TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    descripcion_problema TEXT NOT NULL,
    diagnostico TEXT,
    estado VARCHAR(20) NOT NULL DEFAULT 'PENDIENTE', -- 'PENDIENTE', 'EN_PROCESO', 'COMPLETADO', 'CANCELADO'
    FOREIGN KEY (cliente_id) REFERENCES clientes(id),
    FOREIGN KEY (vehiculo_id) REFERENCES vehiculos(id)
);

CREATE TABLE orden_repuestos (
    orden_id INT NOT NULL,
    repuesto_id INT NOT NULL,
    cantidad INT NOT NULL,
    precio_unitario DECIMAL(10,2) NOT NULL,
    PRIMARY KEY (orden_id, repuesto_id),
    FOREIGN KEY (orden_id) REFERENCES ordenes_servicio(id) ON DELETE CASCADE,
    FOREIGN KEY (repuesto_id) REFERENCES repuestos(id)
);

Credencial!
INSERT INTO usuarios (username, password, role, estado, created_at) 
VALUES ('admin', 'admin123', 'ADMIN', 'ACTIVO', CURRENT_TIMESTAMP);

INSERT INTO usuarios (username, password, role, estado, created_at) 
VALUES ('recepcion', '12345', 'RECEPCIONISTA', 'ACTIVO', CURRENT_TIMESTAMP);

```

## 🔗 Formato de la URL de conexión JDBC
La URL de conexión utiliza el siguiente formato:
```
jdbc:postgresql://HOST:PUERTO/BASE_DE_DATOS
```

## ▶️ Ejecución

### Desde un IDE

1. Abre el proyecto en tu IDE preferido (*IntelliJ IDEA*, *NetBeans*, *Eclipse* o *VS Code*).
2. Verifica que el servicio de **PostgreSQL** esté iniciado en tu sistema.
3. Comprueba que las credenciales (`URL`, `usuario` y `contraseña`) en la clase `DatabaseConnection` sean correctas.
4. Asegúrate de que la base de datos y la tabla correspondiente ya hayan sido creadas.
5. Ejecuta la clase `Main.java`.



## 📷 Capturas de pantalla de la Interfaz.
<img width="480" height="270" alt="image" src="https://github.com/user-attachments/assets/11cc1ad5-266c-4d85-8992-deee2597ddff" />

## Diagramas de Clases. 
1. Diagrama de Clases (UML)
Este diagrama muestra cómo interactúan la Vista, los Controladores, la cadena de Decoradores de Servicio y la capa de Persistencia (Repositorios):

```
                      +-------------------+
                      |   TallerExpress   |
                      |      (Main)       |
                      +---------+---------+
                                |
             +------------------+------------------+
             |                                     |
             v                                     v
     +---------------+                     +---------------+
     |   LoginView   |                     |OrdenServicioView
     +-------+-------+                     +-------+-------+
             |                                     |
             v                                     v
  +--------------------+               +-----------------------+
  | UsuarioController  |               |  OrdenServicioCtrl   |
  +----------+---------+               +-----------+-----------+
             |                                     |
             v                                     v
   <<interface>>                        <<interface>>
  +------------------+                 +-----------------------+
  |  UsuarioService  |                 | OrdenServicioService  |
  +------------------+                 +-----------------------+
           ^                                       ^
           |                                       |
 +---------+------------------+                    |
 |                            |                    |
 |  +----------------------+  |                    |
 |  | HttpLoggerDecorator  |  |                    |
 |  +----------+-----------+  |                    |
 |             | (wraps)      |                    |
 |             v              |                    |
 |  +----------------------+  |                    |
 |--|DefaultValuesDecorator|  |                    |
 |  +----------+-----------+  |                    |
 |             | (wraps)      |                    |
 |             v              |                    |
 |  +----------------------+  |            +-------+---------------+
 +--|  UsuarioServiceImpl  |  |            |OrdenServicioServiceImpl|
    +----------+-----------+  |            +-----------+-----------+
               |              |                        |
               v              +                        v
      <<interface>>                              <<interface>>
  +-------------------+                      +-------------------+
  | UsuarioRepository |                      | OrdenServicioRepo |
  +---------+---------+                      +---------+---------+
            ^                                          ^
            |                                          |
  +---------+---------+                      +---------+---------+
  |UsuarioRepoImpl    |                      |OrdenServicioRepoImpl|
  |  (PostgreSQL)     |                      |   (PostgreSQL)    |
  +-------------------+                      +-------------------+
```


## Diagrama de Casos de Uso. 
```
                        +-------------------------------------------------------------+
                        |                      Taller Express                         |
                        +-------------------------------------------------------------+
                                                       |
     +-------------------+                             |                             +--------------------+
     |                   |--- (CU01: Iniciar Sesión) --+                             |                    |
     |                   |                             |                             |                    |
     |                   |--- (CU02: Registrar Cliente)+                             |                    |
     |                   |                             |                             |                    |
     |                   |--- (CU03: Asociar Vehículo) +                             |                    |
     |   Recepcionista   |    <<include>>              |                             |                    |
     |     (Actor)       |-----> (CU04: Crear Orden de Servicio)                     |     PostgreSQL     |
     |                   |          |                  |                             |  (Sistema Externo) |
     |                   |          |                  |                             |                    |
     |                   |          | <<include>>      |                             |                    |
     |                   |          v                  |                             |                    |
     |                   |--- (CU05: Agregar Repuestos a Orden)                      |                    |
     |                   |                             |                             |                    |
     |                   |--- (CU06: Consultar Historial por Vehículo)               |                    |
     +-------------------+                             |                             +--------------------+
               ^                                       |                                       ^
               | (Hereda)                              |                                       |
               |                                       |                                       |
     +-------------------+                             |                                       |
     |      Admin        |--- (CU07: Inactivar Cliente)+---------------------------------------+
     |     (Actor)       |                             | (Persistencia de Datos)
     |                   |--- (CU08: Gestionar Inventario Repuestos)
     +-------------------+                             |
                                                       |
                        +-------------------------------------------------------------+
```

## 📄 Licencia

Este proyecto puede utilizarse con fines educativos y puede modificarse libremente para adaptarse a las necesidades de cada aplicación.

---

## 👨‍💻 Autor

Desarrollado con ☕ **Java**, 💙 esfuerzo y 🐘 **PostgreSQL** por **Daniel Viaña**.
