# Proyecto 1 Programación 3

## Library Management System (Java + Interfaz gráfica)

Este proyecto es una evolución del sistema de gestión de bibliotecas realizado para el curso "Programación 3, EIF206, I ciclo 2025" en la Universidad Nacional de Costa Rica. Está implementado en Java e integra una interfaz gráfica que facilita la administración de recursos como libros, revistas, artículos, videos, usuarios y préstamos.

<!--
CAPTURAS DE PANTALLA DE LA INTERFAZ
Coloque aquí imágenes de ejemplo de la interfaz gráfica del sistema (ventana principal, gestión de materiales, usuarios, préstamos y reportes).
Ejemplos sugeridos:
- Menú principal
- Formulario de registro/edición de materiales
- Formulario de registro/edición de usuarios
- Ventana de préstamos/devoluciones
- Reportes y búsqueda/filtrado
-->

## Tabla de Contenidos

- [Características](#características)
- [Requisitos](#requisitos)
- [Instalación](#instalación)
- [Uso](#uso)
- [Estructura de Archivos](#estructura-de-archivos)
- [Contribuciones](#contribuciones)
- [Licencia](#licencia)

## Características

- **Gestión de Materiales:** Añadir, modificar y eliminar materiales (libros, revistas, artículos, videos físicos y digitales).
- **Gestión de Usuarios:** Registrar, modificar y eliminar usuarios, con historial de préstamos.
- **Sistema de Préstamos:** Préstamo y devolución de materiales con validaciones y control de disponibilidad.
- **Reportes:** Reportes de materiales, usuarios y préstamos; filtros y búsquedas.
- **Gestión de Tiempo:** Simulación del avance temporal para controlar vencimientos y moras.
- **Persistencia de Datos:** Guardado y carga de información desde archivos.
- **Interfaz Gráfica:** Menús y ventanas intuitivas, validaciones y manejo de errores.
- **Arquitectura por Capas (MVC):** Separación clara entre capa de presentación, lógica/servicios y datos.

## Requisitos

- **Java 21** (recomendado) o superior.
- Interfaz gráfica en **Swing**:.
  - Si usa Swing: incluido en el JDK.
- IDE recomendado: IntelliJ IDEA / Eclipse / VS Code (con extensiones Java).
- Opcional Maven para la gestión de dependencias y empaquetado.

## Instalación

1. Clonar el repositorio:
   ```
   git clone https://github.com/Bloodiewormer/Proyecto-1-Progra-3.git
   ```
2. Abra el proyecto en su IDE y seleccione el SDK de Java (17+).
3. Configure el toolkit gráfico utilizado:
   - Swing : no requiere configuración adicional.
4. Compile el proyecto.

Opcional (línea de comandos):
- Maven (JavaFX con plugin):
  ```
  mvn clean javafx:run
  ```
- Maven (JAR ejecutable):
  ```
  mvn clean package
  java -jar target/<nombre-del-jar>.jar
  ```

## Uso

1. Ejecute la clase principal (por ejemplo, `Main.java` o `App.java` si es JavaFX).
2. La aplicación mostrará la **ventana principal** con opciones como:
   - **Gestión de Materiales**
   - **Gestión de Usuarios**
   - **Préstamos**
   - **Reportes**
   - **Guardar Datos**
   - **Salir**
3. Navegue por los menús/ventanas para realizar operaciones. Las validaciones informarán de entradas inválidas o acciones no permitidas.
4. Use la opción de **Guardar Datos** para persistir la información antes de cerrar.

## Estructura de Archivos

La estructura puede variar, pero se sugiere una organización por capas (MVC):

- `src/main/java/`
  - `ui/` o `view/`: Ventanas/pantallas (JavaFX: escenas/controladores; Swing: frames/panels).
  - `controller/`: Coordinación entre la UI y la lógica de negocio.
  - `model/`: Entidades (Usuario, Libro, Revista, Articulo, Video, Prestamo, etc.).
  - `service/`: Reglas de negocio (gestión de materiales, usuarios, préstamos, reportes).
  - `persistence/`: Lectura/escritura de archivos (persistencia).
  - `util/`: Utilidades y helpers.
  - `Main.java` o `App.java`: Punto de entrada de la aplicación.
- `src/main/resources/`:
  - Archivos FXML/CSS (si JavaFX), íconos, y otros recursos.
- `data/` (opcional):
  - Archivos de datos para persistencia.

## Contribuciones

¡Las contribuciones son bienvenidas! Si desea mejorar el proyecto o corregir errores:
1. Haga un fork del repositorio.
2. Cree una rama descriptiva (`feature/nueva-funcionalidad` o `fix/bug-x`).
3. Envíe un Pull Request con una descripción clara de los cambios.

## Licencia

Este proyecto está bajo la Licencia MIT. Consulte el archivo [LICENSE](LICENSE) para más detalles.
