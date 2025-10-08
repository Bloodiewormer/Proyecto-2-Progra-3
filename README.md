# Sistema de Prescripción y Despacho de Recetas — Proyecto 2 (EIF206 2025-II)


Este proyecto es la evolución del Proyecto #1, migrado a una arquitectura distribuida con:
- Frontend (presentación, MVC) en Java que se conecta a un Backend por sockets.
- Backend (lógica + datos) en Java que expone un servicio por sockets TCP y persiste en MySQL.
- Comunicación por sockets con notificaciones asíncronas para login/logout y mensajería tipo chat.
- Uso del patrón Proxy en el Frontend para invocar al Service remoto del Backend.

Nota: Este repositorio parte de la base del Proyecto #1 y se reorganizará para cumplir estrictamente con la arquitectura distribuida y la persistencia en base de datos.

## Tabla de contenidos
- [Arquitectura](#arquitectura)
- [Requisitos](#requisitos)
- [Estructura de proyecto (propuesta)](#estructura-de-proyecto-propuesta)
- [Configuración](#configuración)
- [Base de datos](#base-de-datos)
- [Protocolo de red (mensajes)](#protocolo-de-red-mensajes)
- [Ejecución](#ejecución)
- [Funcionalidades (rúbrica)](#funcionalidades-rúbrica)
- [Validación, seguridad y calidad](#validación-seguridad-y-calidad)
- [Roadmap](#roadmap)
- [Créditos](#créditos)
- [Licencia](#licencia)

## Arquitectura
- Frontend:
  - Java (recomendado: JavaFX para la UI), patrón MVC.
  - No tiene ninguna dependencia ni referencia a la base de datos.
  - Usa un Proxy (clase local) que envía/recibe peticiones por sockets al Backend.
  - Recibe notificaciones asíncronas (usuarios activos, mensajes tipo chat).
- Backend:
  - Java (ServerSocket / NIO), diseño multihilo para atender múltiples Frontend.
  - Capas: Service (lógica de negocio) + Datos (DAO/Repositorio) con MySQL vía JDBC.
  - Único responsable de acceder a la base de datos.
  - Emite notificaciones asíncronas a los Frontend conectados.
- Comunicación:
  - Sockets TCP.
  - Formato de mensajes recomendado: JSON por línea (JSON Lines), UTF-8.
  - Autenticación, autorización por rol, y manejo de sesión por conexión.
- Persistencia:
  - MySQL 8.x con esquema normalizado.
  - Scripts SQL versionados en carpeta `db/`.

## Requisitos
- Java 17+ (o la versión definida por la cátedra).
- Maven 3.9+ o Gradle (se recomienda Maven multi-módulo).
- MySQL 8.x.
- Variables de entorno o archivo de propiedades para credenciales de BD.

## Estructura de proyecto (propuesta)
Se propone migrar a una estructura multi-módulo para separar claramente capas y reutilizar contratos/datos compartidos:

```
Proyecto-2-Progra-3/
├─ common/                 # DTOs, contratos de servicio, constantes de protocolo
│  └─ src/main/java/...
├─ backend/                # Servidor (Service + DAO/Repositorio + notificaciones)
│  ├─ src/main/java/...
│  └─ src/main/resources/application.properties
├─ frontend/               # Cliente (UI MVC + Proxy)
│  ├─ src/main/java/...
│  └─ src/main/resources/application.properties
├─ db/
│  ├─ schema.sql
│  └─ seed.sql
├─ docs/
│  └─ protocolo.md
├─ README.md
└─ TODO.md
```

Si ya tienes código del Proyecto #1, se refactoriza y redistribuye para ajustarse a esta estructura.

## Configuración
- Backend (`backend/src/main/resources/application.properties`):
  ```
  server.port=5050
  db.url=jdbc:mysql://localhost:3306/recetas?useSSL=false&serverTimezone=UTC
  db.user=tu_usuario
  db.password=tu_password
  security.password.hashRounds=10
  ```
- Frontend (`frontend/src/main/resources/application.properties`):
  ```
  server.host=127.0.0.1
  server.port=5050
  ui.theme=light
  ```

Usa variables de entorno si prefieres no versionar credenciales (e.g., en tiempo de ejecución o con un wrapper).

## Base de datos
Esquema sugerido (resumen):
- `users` (id, username, password_hash, role [ADMIN, MEDIC, PHARMACIST, PATIENT], is_active, created_at, updated_at)
- `patients` (id, person_data…)
- `medics` (id, person_data…, specialty)
- `pharmacists` (id, person_data…)
- `medications` (id, code, name, stock, unit, price, is_active)
- `prescriptions` (id, code, patient_id, medic_id, created_at, status [NEW, DISPENSED, CANCELLED])
- `prescription_items` (id, prescription_id, medication_id, quantity, indications)
- `dispenses` (id, prescription_id, pharmacist_id, dispensed_at)
- `messages` (id, sender_user_id, recipient_user_id, text, status [SENT, RECEIVED], created_at)

Notas:
- Para roles, puedes usar una única tabla `users` con columna `role` en lugar de tablas separadas, según lo prefieras.
- Usa índices y claves foráneas.
- Incluye datos de ejemplo en `seed.sql` para pruebas.

## Protocolo de red (mensajes)
Formato recomendado: un JSON por línea. Ejemplo:
```
{ "op":"AUTH_LOGIN", "data": { "username":"alice", "password":"****" } }
```

Operaciones solicitadas por el Frontend (sincrónicas):
- AUTH_LOGIN, AUTH_CHANGE_PASSWORD
- USERS_LIST_ACTIVE
- USERS_LIST (ADMIN), USERS_CREATE/UPDATE/DELETE (ADMIN)
- MEDICS_LIST
- PHARMACISTS_LIST
- PATIENTS_LIST
- DRUGS_LIST
- PRESCRIPTION_CREATE
- PRESCRIPTIONS_HISTORY_BY_PATIENT / BY_MEDIC
- DISPENSE_PRESCRIPTION
- DASHBOARD_METRICS
- MESSAGE_SEND
- MESSAGE_RECEIVE (pull para marcar como recibido/abrir ventana)

Notificaciones del Backend (asíncronas, broadcast según corresponda):
- USER_LOGIN, USER_LOGOUT
- MESSAGE_DELIVERED (cuando llega un mensaje para un usuario)
- STOCK_UPDATED (opcional, si el stock cambia tras despacho)

Cada mensaje debe incluir:
- `op`: operación
- `data`: carga útil
- `reqId`: correlación (opcional pero recomendado)
- `auth`: token de sesión o info de usuario autenticado (según diseño)
- `error`: nulo o detalle de error con código/mensaje

## Ejecución
1) Backend:
- Configura MySQL y ejecuta `db/schema.sql` y `db/seed.sql`.
- Ajusta `backend/src/main/resources/application.properties`.
- Compila y ejecuta el Backend:
  - Maven: `mvn -pl backend -am clean package` y luego `java -jar backend/target/backend-*.jar`

2) Frontend:
- Ajusta `frontend/src/main/resources/application.properties` (host/puerto del backend).
- Compila y ejecuta:
  - Maven: `mvn -pl frontend -am clean javafx:run` o `java -jar frontend/target/frontend-*.jar` si empacas.

3) Pruebas básicas:
- Hacer login con usuarios de `seed.sql`.
- Ver “Usuarios activos”.
- Enviar mensaje a otro usuario activo y recibirlo.
- Crear una receta y luego despacharla (con impacto en stock).
- Ver Dashboard.

## Funcionalidades (rúbrica)
- 1. Ingreso (login) y cambio de clave
- 2. Prescripción
- 3. Despacho
- 4. Lista de Médicos
- 5. Lista de Farmacéuticos
- 6. Lista de Pacientes
- 7. Catálogo de medicamentos
- 8. Dashboard (Indicadores)
- 9. Histórico de recetas
- 10. Usuarios
- 11. Mostrar y recibir mensajes

Consulta el archivo [TODO.md](./TODO.md) para el plan detallado de implementación.

## Validación, seguridad y calidad
- Validación en Frontend y Backend (reglas de negocio y de formato).
- Manejo robusto de excepciones y mensajes de error claros al usuario.
- Hash de contraseñas (BCrypt o PBKDF2).
- Logging con niveles (INFO/WARN/ERROR) en servidor, logs rotados.
- Pruebas unitarias (Service/DAO) y pruebas de integración (protocolo y BD).
- Concurrencia: servidor multihilo, cuidado con sincronización de listas de usuarios y notificaciones.
- Pool de conexiones JDBC (e.g., HikariCP) recomendado.

## Roadmap
- Migrar el código del Proyecto #1 a la estructura multi-módulo.
- Implementar protocolo base (login, usuarios activos, mensajería).
- Implementar CRUDs (médicos, farmacéuticos, pacientes, medicamentos).
- Implementar prescripción y despacho con actualización de stock.
- Implementar histórico y dashboard.
- Pruebas, validaciones, manejo de errores.
- Documentación final y empaquetado de entrega.

## Créditos
- Universidad Nacional — EIF206 Programación 3 (2025-II)
- Equipo del Proyecto #1 (continuación en Proyecto #2)

## Licencia
Proyecto académico. Si no se especifica otra licencia, “All rights reserved” para efectos del curso.
