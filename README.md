# Proyecto 1 Programación 3

## Library Management System (Versión avanzada con interfaz gráfica)

Este proyecto es una evolución del sistema de gestión de bibliotecas realizado para el curso "Programación 3, EIF304, II ciclo 2025" en la Universidad Nacional de Costa Rica. Integra una interfaz gráfica que facilita la administración de recursos como libros, revistas, artículos, videos, usuarios y préstamos.

> **Nota:** Aquí deben incluirse imágenes de ejemplo de la interfaz gráfica del sistema.  
> *(Coloque capturas de pantalla o gifs mostrando los menús principales, gestión de materiales y reportes.)*

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
- **Gestión de Usuarios:** Registrar, modificar y eliminar usuarios, así como su historial de préstamos.
- **Sistema de Préstamos:** Préstamo y devolución de materiales con seguimiento y validación.
- **Reportes:** Generar reportes detallados de materiales, usuarios y préstamos.
- **Gestión de Tiempo:** Simulación del avance temporal para controlar vencimientos.
- **Persistencia de Datos:** Guardado y carga de la información mediante archivos.
- **Interfaz Gráfica:** Menús intuitivos y navegación mediante ventanas y botones.

## Requisitos

- **C++17** o superior.
- **Biblioteca gráfica recomendada:** Qt, wxWidgets o similar (ver instrucciones específicas del proyecto).
- **Gestión eficiente de memoria dinámica.**
- **Interfaz amigable:** Menús gráficos con validación y manejo de errores.

## Instalación

1. Clone el repositorio:
   ```
   git clone https://github.com/Bloodiewormer/Proyecto-1-Progra-3.git
   ```
2. Abra el proyecto en su IDE favorito compatible con C++ y la biblioteca gráfica seleccionada.
3. Instale las dependencias gráficas si es necesario (por ejemplo, Qt).
4. Compile el proyecto.

## Uso

1. Ejecute el archivo principal (por ejemplo, `main.cpp`).
2. El sistema mostrará la **ventana principal** con opciones como:
   - **Gestión de Materiales**
   - **Gestión de Usuarios**
   - **Préstamos**
   - **Reportes**
   - **Guardar Datos**
   - **Salir**
3. Navegue por los menús gráficos para operar el sistema.

## Estructura de Archivos

- **main.cpp**: Punto de entrada del programa.
- **Controladora.cpp/h**: Lógica principal del negocio.
- **InterfazGrafica.cpp/h**: Manejo de la interfaz gráfica y menús.
- **Usuario.cpp/h**: Gestión de usuarios.
- **Libro.cpp/h, Revista.cpp/h, Articulo.cpp/h, Video.cpp/h**: Clases para los diferentes tipos de materiales.
- **GestorArchivos.cpp/h**: Persistencia de datos.
- **Otros archivos**: Recursos e imágenes de la interfaz.

## Contribuciones

¡Las contribuciones son bienvenidas! Si desea mejorar el proyecto o corregir errores, por favor haga un fork y envíe un pull request.

## Licencia

Este proyecto está bajo la Licencia MIT. Consulte el archivo [LICENSE](LICENSE) para más detalles.
