# Proyecto #2 — Sistema de Prescripción y Despacho de Recetas (Arquitectura distribuida: Java, Sockets, Proxy, MySQL)

Universidad Nacional — Facultad de Ciencias Exactas y Naturales — Escuela de Informática  
Curso: EIF206 Programación 3 (2025-II)

Evolución del [Proyecto #1](https://github.com/Bloodiewormer/Proyecto-1-Progra-3), ahora con:
- Frontend en Java (MVC) conectado a un Backend por sockets TCP.
- Backend en Java (Service + DAO) con persistencia en MySQL.
- Patrón Proxy en el Frontend para invocar servicios remotos.
- Notificaciones en tiempo real de usuarios activos y mensajería tipo chat.

---

## Tabla de Contenidos
- [Requisitos](#requisitos)
- [Arquitectura y estructura de carpetas](#arquitectura-y-estructura-de-carpetas)
- [Configuración](#configuración)
- [Base de datos](#base-de-datos)
- [Protocolo de red (mensajes)](#protocolo-de-red-mensajes)
- [Cómo ejecutar](#cómo-ejecutar)
- [Funcionalidades](#funcionalidades)
- [Notificaciones y mensajería](#notificaciones-y-mensajería)
- [Validaciones](#validaciones)
- [Capturas de pantalla (placeholders)](#capturas-de-pantalla-placeholders)
- [Roadmap y rúbrica](#roadmap-y-rúbrica)
- [Convenciones](#convenciones)
- [Licencia](#licencia)

---

## Requisitos
- Java JDK 21 o superior
- IDE recomendado: IntelliJ IDEA / Eclipse / NetBeans
- MySQL 8.x
- Maven 
- Acceso a red local para la comunicación por sockets (puertos configurables)

---

## Arquitectura y estructura de carpetas
Arquitectura distribuida con separación estricta de responsabilidades:

- Frontend (cliente UI + MVC + Proxy)
- Backend (Service/Reglas + DAO/Repositorio + Notificaciones)
- Persistencia en MySQL (únicamente desde el Backend)

Estructura actual del repo:
```
Proyecto-2-Progra-3/
├─ JavaMVCBackEnd/     # Servidor: lógica de negocio + acceso a datos (MySQL) + notificaciones
└─ JavaMVCFrontEnd/    # Cliente: UI MVC + Proxy (comunicación con backend por sockets)
```

Referencias:
- Backend: [JavaMVCBackEnd](https://github.com/Bloodiewormer/Proyecto-2-Progra-3/tree/main/JavaMVCBackEnd)
- Frontend: [JavaMVCFrontEnd](https://github.com/Bloodiewormer/Proyecto-2-Progra-3/tree/main/JavaMVCFrontEnd)

Sugerencia de paquetes (ajusta a tu código actual):
```
com/una/eif206/recetas/
  common/           # DTOs, constantes, contratos (si se comparten)
  backend/
    server/         # Aceptación de conexiones, manejo de sesiones
    service/        # Casos de uso/reglas de negocio
    repository/     # DAO/JDBC a MySQL
    notifier/       # Gestión de eventos/usuarios activos/mensajes
    config/         # Carga de properties
  frontend/
    ui/             # Vistas (JavaFX/Swing)
    controller/     # Controladores MVC
    proxy/          # Cliente Proxy (cliente TCP)
    model/          # Modelos locales
    util/           # Utilitarios y validaciones
```

---

## Configuración
Archivos de propiedades recomendados:

- Backend (ejemplo `application.properties`):
```
server.port=5050
db.url=jdbc:mysql://localhost:3306/recetas?useSSL=false&serverTimezone=UTC
db.user=tu_usuario
db.password=tu_password
security.password.hashRounds=10
```

- Frontend (ejemplo `application.properties`):
```
server.host=127.0.0.1
server.port=5050
ui.theme=light
```

Usa variables de entorno o un gestor seguro para credenciales de BD si lo prefieres.

---

## Base de datos
Esquema sugerido (resumen):
- `users` (id, username, password_hash, role [ADMIN, MEDIC, PHARMACIST, PATIENT], is_active, created_at, updated_at)
- `patients` (id, nombre, fecha_nacimiento, telefono, etc.)
- `medics` (id, persona, especialidad)
- `pharmacists` (id, persona)
- `medications` (id, code, name, stock, unit, price, is_active)
- `prescriptions` (id, code, patient_id, medic_id, created_at, status [CREATED, IN_PROCESS, READY, DELIVERED])
- `prescription_items` (id, prescription_id, medication_id, quantity, indications, duration_days)
- `dispenses` (id, prescription_id, pharmacist_id, dispensed_at)
- `messages` (id, sender_user_id, recipient_user_id, text, status [SENT, RECEIVED], created_at)

Notas:
- Roles y autenticación centralizados en `users` con `role`.
- Usa claves foráneas e índices.
- Incluye datos de ejemplo (usuarios, medicamentos) para pruebas iniciales.

Credenciales de prueba (ejemplo):
- Administrador: `admin` / `admin`
- Médico: `m001` / `m001`
- Farmacéutico: `f001` / `f001`

Ajusta estos datos a los que hayas cargado en tu `seed`.

---

## Protocolo de red (mensajes)
Comunicación por sockets TCP con mensajes JSON por línea (JSON Lines, UTF-8).

Ejemplo:
```
{ "op":"AUTH_LOGIN", "data": { "username":"admin", "password":"admin" }, "reqId":"1234" }
```

Operaciones síncronas (solicitadas por el Frontend):
- AUTH_LOGIN, AUTH_CHANGE_PASSWORD
- USERS_LIST_ACTIVE
- USERS_LIST (ADMIN), USERS_CREATE/UPDATE/DELETE (ADMIN)
- MEDICS_LIST, PHARMACISTS_LIST, PATIENTS_LIST, DRUGS_LIST
- PRESCRIPTION_CREATE
- PRESCRIPTIONS_HISTORY_BY_PATIENT / BY_MEDIC
- DISPENSE_PRESCRIPTION
- DASHBOARD_METRICS
- MESSAGE_SEND, MESSAGE_RECEIVE

Notificaciones asíncronas (emite el Backend):
- USER_LOGIN, USER_LOGOUT
- MESSAGE_DELIVERED
- STOCK_UPDATED (opcional)

Cada mensaje incluye: `op`, `data`, `reqId` (opcional), `auth` (según sesión), `error` (si aplica).

---

## Cómo ejecutar
1) Base de datos
- Crea la base `recetas` en MySQL.
- Ejecuta tu script de esquema y de datos (usuarios/medicamentos de ejemplo).

2) Backend
- Configura `db.url`, `db.user`, `db.password` y `server.port`.
- Ejecuta desde el IDE la clase principal del servidor
  (por ejemplo: `com.una.eif206.recetas.backend.server.MainServer`).
- Alternativa con Maven/Gradle (si aplica): compila y ejecuta el módulo Backend.

3) Frontend
- Configura `server.host` y `server.port` para apuntar al Backend.
- Ejecuta desde el IDE la clase principal de la UI
  (por ejemplo: `com.una.eif206.recetas.frontend.ui.MainApp`).
- Alternativa con Maven/Gradle (si aplica): compila y ejecuta el módulo Frontend.

Pruebas rápidas
- Login con usuarios de ejemplo.
- Ver “Usuarios activos”.
- Enviar/recibir un mensaje de chat.
- Crear receta, avanzar flujo de despacho, verificar impacto en stock.
- Consultar Dashboard e Histórico.

---

## Funcionalidades
1) Ingreso (login) y cambio de clave  
2) Prescripción (MÉDICO)  
- Búsqueda de pacientes y medicamentos (aproximada, case-insensitive)
- Detalle con cantidad, indicaciones y duración en días
- Estados: `CREATED`

3) Despacho (FARMACÉUTICO)  
- Ventana de retiro válida (hoy ±3 días, si aplica)
- Flujo: `IN_PROCESS` → `READY` → `DELIVERED`
- Actualización de stock

4) Administración (ADMIN)  
- CRUD Médicos, Farmacéuticos, Pacientes
- Catálogo de Medicamentos (CRUD, búsqueda)
- Usuarios (crear, listar, activar/desactivar, reset de clave)

5) Dashboard (indicadores)  
- Línea: unidades prescritas por mes y por medicamento
- Pastel: recetas por estado

6) Histórico de recetas  
- Filtros por paciente, médico, fechas, estado
- Vista detalle de receta

7) Usuarios activos y mensajería tipo chat  
- Presencia en tiempo real
- Envío/recepción de mensajes entre usuarios conectados

---

## Notificaciones y mensajería
- Presencia: el Backend emite `USER_LOGIN` y `USER_LOGOUT` a todos los clientes.
- Mensajería: `MESSAGE_DELIVERED` al destinatario; el Frontend muestra/gestiona lectura.
- Manejo de concurrencia: colas/handlers por conexión; sincronización de listas de usuarios.

---

## Validaciones
- Autenticación: usuario/clave obligatorios; hash seguro (BCrypt/PBKDF2).
- Prescripción: paciente válido; detalles con cantidad > 0, indicaciones no vacías, duración > 0.
- Búsquedas: case-insensitive, por “contiene”.
- Despacho: transición de estados válida y dentro de ventana de retiro.
- CRUDs: ids únicos; formatos y longitudes; datos obligatorios.
- Errores: mensajes claros hacia UI; logging en servidor.

---

## Capturas de pantalla (placeholders)
- Login  
  ![Login](docs/images/login.png)

- Usuarios Activos  
  ![Usuarios Activos](docs/images/usuarios-activos.png)

- Chat  
  ![Chat](docs/images/chat.png)

- Dashboard  
  ![Dashboard](docs/images/dashboard.png)

- Prescripción  
  ![Prescripción](docs/images/prescribir.png)

- Despacho  
  ![Despacho](docs/images/despacho.png)

Sugerencia: exporta capturas a 1280px de ancho para consistencia.

---

## Roadmap y rúbrica
Estado de implementación (Proyecto terminado):

- [x] 1. Login y cambio de clave
- [x] 2. Prescripción
- [x] 3. Despacho
- [x] 4. Lista de Médicos
- [x] 5. Lista de Farmacéuticos
- [x] 6. Lista de Pacientes
- [x] 7. Catálogo de medicamentos
- [x] 8. Dashboard (Indicadores)
- [x] 9. Histórico de recetas
- [x] 10. Usuarios (gestión)
- [x] 11. Mostrar y recibir mensajes (chat)

Extras recomendados:
- [x] Validaciones robustas y manejo de errores
- [x] Logging y niveles en servidor
- [x] Pool de conexiones JDBC (e.g., HikariCP)
- [x] Pruebas unitarias/integración básicas

---

## Convenciones
- Idioma: Español (es-CR)
- Fechas: ISO-8601 `YYYY-MM-DD`
- Codificación: UTF-8
- Commits: mensaje corto en imperativo + descripción si aplica
- Estados de receta: `CREATED` → `IN_PROCESS` → `READY` → `DELIVERED`

---

## Licencia
MIT
