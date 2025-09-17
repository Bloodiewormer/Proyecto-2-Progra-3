# Proyecto #1 — Sistema de Prescripción y Despacho de Recetas (Java, MVC, XML)

Universidad Nacional — Facultad de Ciencias Exactas y Naturales — Escuela de Informática  
Curso: EIF206 Programación 3 (2025-II)

Sistema de escritorio en Java para:
- Prescripción de recetas por parte de médicos.
- Despacho de recetas por personal de farmacia.
- Administración de usuarios (médicos, farmaceutas, pacientes) y catálogo de medicamentos.
- Dashboard de indicadores y consulta histórica.

La aplicación utiliza:
- Interfaz gráfica (Swing/JavaFX, según implementación del proyecto).
- Arquitectura por capas y patrón Modelo–Vista–Controlador (MVC).
- Persistencia en archivos XML.

---

## Tabla de Contenidos
- [Requisitos](#requisitos)
- [Arquitectura y estructura de carpetas](#arquitectura-y-estructura-de-carpetas)
- [Datos iniciales (XML) y usuarios de prueba](#datos-iniciales-xml-y-usuarios-de-prueba)
- [Cómo ejecutar](#cómo-ejecutar)
- [Funcionalidades](#funcionalidades)
- [Dashboard (Indicadores)](#dashboard-indicadores)
- [Histórico de recetas](#histórico-de-recetas)
- [Validaciones](#validaciones)
- [Capturas de pantalla (placeholders)](#capturas-de-pantalla-placeholders)
- [Roadmap y rúbrica](#roadmap-y-rúbrica)
- [Convenciones](#convenciones)
- [Licencia](#licencia)

---

## Requisitos
- Java JDK 17 o superior
- IDE recomendado: IntelliJ IDEA / Eclipse / NetBeans
- (Opcional) Maven o Gradle si el proyecto ya está configurado con alguno
- Sistema de archivos con permisos de lectura/escritura para la carpeta de datos XML

---

## Arquitectura y estructura de carpetas
Arquitectura por capas y patrón MVC:

- Capa dominio (entidades): `domain/`
- Capa repositorio (XML): `repository/` o `persistence/xml/`
- Capa servicios (lógica de negocio): `service/`
- Capa controladores (MVC): `controller/`
- Capa vistas (MVC): `ui/` (formularios/pantallas)
- Utilitarios y validaciones: `util/`
- Configuración y constantes: `config/`

Sugerencia de estructura (ajústala a la del proyecto si ya existe):
```
src/
  main/
    java/
      com/una/eif206/recetas/
        domain/
        repository/
        service/
        controller/
        ui/
        util/
        config/
    resources/
      data/
        usuarios.xml
        pacientes.xml
        medicamentos.xml
        recetas.xml
      i18n/
  test/
docs/
  images/
```

---

## Datos iniciales (XML) y usuarios de prueba
Se incluyen archivos XML de ejemplo en `src/main/resources/data/`:
- `usuarios.xml`: usuarios de prueba (ADMIN, MÉDICO, FARMACEUTA)
- `pacientes.xml`: pacientes iniciales
- `medicamentos.xml`: catálogo inicial
- `recetas.xml`: ejemplo de receta en estado “confeccionada”

Credenciales de prueba:
- Administrador
  - id: `admin`
  - clave: `admin`
- Médicos
  - id: `m001` (Dra. Ana Pérez) — clave: `m001`
  - id: `m002` (Dr. Carlos Gómez) — clave: `m002`
- Farmaceuta
  - id: `f001` (Farm. Juan Mora) — clave: `f001`

Notas:
- Al crear un usuario nuevo, la clave debe quedar igual al id por defecto, según los requerimientos.
- Roles permitidos: `ADMIN`, `MEDICO`, `FARMACEUTA`.
- Estados de receta: `confeccionada`, `proceso`, `lista`, `entregada`.

Rutas de datos (puedes ajustarlas en tu código si usas otra ubicación):
- Producción/IDE: `src/main/resources/data/`
- En tiempo de ejecución, asegúrate de que el código resuelva correctamente el path de recursos.

---

## Cómo ejecutar
- Opción A: IDE
  1) Importa el proyecto como proyecto Java/Maven/Gradle.
  2) Configura el SDK (JDK 17+).
  3) Ejecuta la clase principal `Main` de tu aplicación (por ejemplo: `com.una.eif206.recetas.ui.Main`).

- Opción B: Maven (si aplica)
  - Compilar: `mvn clean package`
  - Ejecutar: `mvn exec:java -Dexec.mainClass="com.una.eif206.recetas.ui.Main"`

- Opción C: Gradle (si aplica)
  - Compilar: `./gradlew build`
  - Ejecutar: `./gradlew run` (si está configurado el plugin de aplicación)

---

## Funcionalidades
1) Ingreso (login) y cambio de clave
- Login por id/clave.
- Cambio de clave para cualquier usuario autenticado.

2) Prescripción (solo MÉDICO)
- Búsqueda aproximada de paciente por id/nombre.
- Búsqueda aproximada de medicamento por código/descripción (presentaciones con código distinto).
- Agregar, modificar, eliminar detalles (cantidad, indicaciones, duración en días).
- Guardar receta con `fecha_confeccion` (hoy) y `fecha_retiro`.
- Estado final al registrar: `confeccionada`.

3) Despacho (solo FARMACEUTA)
- Recibir receta `confeccionada` con fecha de retiro hoy o ±3 días.
- Cambiar a `proceso`, luego a `lista` al alistar.
- Entregar medicamentos y marcar `entregada`.

4) Lista de Médicos (solo ADMIN)
- CRUD + búsqueda (id/nombre).
- Al agregar, clave = id.

5) Lista de Farmaceutas (solo ADMIN)
- CRUD + búsqueda (id/nombre).
- Al agregar, clave = id.

6) Lista de Pacientes (solo ADMIN)
- CRUD + búsqueda (id/nombre).
- Campos: id, nombre, fecha de nacimiento, teléfono.

7) Catálogo de Medicamentos (solo ADMIN)
- CRUD + búsqueda (código/descripcion).
- Campos: código, nombre, presentación.

8) Dashboard (MÉDICO/FARMACEUTA/ADMIN)
- Línea: cantidad de medicamentos prescritos por mes (filtrable por medicamentos y rango de meses).
- Pastel: cantidad de recetas por estado.

9) Histórico de Recetas (MÉDICO/FARMACEUTA/ADMIN)
- Búsquedas y detalle, sin editar.

---

## Dashboard (Indicadores)
- Gráfico de línea: eje X = meses, eje Y = total de unidades prescritas por medicamento seleccionado.
- Gráfico de pastel: distribución de recetas por estado (`confeccionada`, `proceso`, `lista`, `entregada`).
- Filtros recomendados: rango de fechas, médico, paciente, medicamento(s).

---

## Histórico de Recetas
- Lista con filtros por id de receta, paciente, médico, rango de fechas, estado.
- Vista detalle: información general, y detalle de medicamentos (cantidad, indicaciones, duración).

---

## Validaciones
- Login: id/clave no vacíos; usuario existente; clave correcta.
- Prescripción: paciente y fecha de retiro válidos; cada detalle con cantidad (>0), indicaciones (texto), duración_dias (>0).
- Búsquedas aproximadas: case-insensitive y por contiene.
- CRUDs de administración: claves/ids únicas; formatos de fecha; teléfono válido.
- Flujos de despacho respetan estados y ventanas de fecha de retiro (±3 días).

---

## Capturas de pantalla (placeholders)

- Login  
  ![Pantalla de Login](docs/images/login.png)

- Cambiar clave  
  ![Cambiar clave](docs/images/cambiar-clave.png)

- Médicos (Administrador)  
  ![Gestión de Médicos](docs/images/medicos-admin.png)

- Dashboard  
  ![Dashboard](docs/images/dashboard.png)

- Prescribir (Médico)  
  ![Prescribir](docs/images/prescribir.png)

- Prescribir — Buscar Paciente  
  ![Prescribir - Buscar Paciente](docs/images/prescribir-buscar-paciente.png)

- Prescribir — Agregar Medicamento  
  ![Prescribir - Agregar Medicamento](docs/images/prescribir-agregar-medicamento.png)

- Prescribir — Modificar Detalle  
  ![Prescribir - Modificar Detalle](docs/images/prescribir-modificar-detalle.png)

Sugerencia: usa 1280px de ancho para consistencia en el README.

---

## Roadmap y rúbrica
Checklist de implementación (mapea la rúbrica):

- [ ] 1. Login y cambio de clave (10%)
- [ ] 2. Prescripción (20%)
- [ ] 3. Despacho (20%)
- [ ] 4. Lista de Médicos (6%)
- [ ] 5. Lista de Farmaceutas (6%)
- [ ] 6. Lista de Pacientes (6%)
- [ ] 7. Catálogo de Medicamentos (6%)
- [ ] 8. Dashboard (Indicadores) (20%)
- [ ] 9. Histórico de Recetas (6%)

Extras recomendados:
- [ ] Validaciones exhaustivas y mensajes de error claros.
- [ ] Pruebas manuales guiadas (guión de pruebas).
- [ ] Manejo de concurrencia en lectura/escritura XML si aplica.

---

## Convenciones
- Idioma: Español (es-CR).
- Fechas en ISO-8601: `YYYY-MM-DD`.
- Codificación: UTF-8.
- Commits: mensaje corto en imperativo + descripción si es necesario.
- Estados de receta permitidos: `confeccionada` → `proceso` → `lista` → `entregada`.

---

## Licencia
MIT
