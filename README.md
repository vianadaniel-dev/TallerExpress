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

    <groupId>com.ejemplo</groupId>
    <artifactId>gestion-usuarios</artifactId>
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

## Diagramas de Clases. 

## Diagrama de Casos de Uso. 


## 📄 Licencia

Este proyecto puede utilizarse con fines educativos y puede modificarse libremente para adaptarse a las necesidades de cada aplicación.

---

## 👨‍💻 Autor

Desarrollado con ☕ **Java**, 💙 esfuerzo y 🐘 **PostgreSQL** por **Daniel Viaña**.
