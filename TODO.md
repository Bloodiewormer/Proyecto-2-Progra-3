# TODO — Proyecto 2 (Sistema de Prescripción y Despacho de Recetas)

Este plan detalla tareas por componente y se alinea con la rúbrica. Marca con [x] lo que vayas completando.

## 0. Base del repositorio
- [ ] Crear estructura multi-módulo (`common/`, `backend/`, `frontend/`, `db/`, `docs/`).
- [ ] Configurar Maven en padre y módulos (targets, shade o javafx-plugin según aplique).
- [ ] Añadir `.gitignore` (Java, Maven, IDEs).
- [ ] Configurar control de versión de `db/schema.sql` y `db/seed.sql`.
- [ ] Definir `application.properties` en backend y frontend.

## 1. Common (contratos y modelos)
- [ ] Definir DTOs: UserDTO, MessageDTO, MedicationDTO, PrescriptionDTO, PrescriptionItemDTO, DashboardDTO…
- [ ] Definir contratos/Interfaces del Service remoto (lo que el Proxy invocará).
- [ ] Definir constantes de protocolo (`op` codes) y errores estandarizados.
- [ ] Serialización/deserialización JSON (Gson/Jackson) consistente.

## 2. Backend — Infraestructura
- [ ] Servidor de sockets (ServerSocket/NIO) en puerto configurable.
- [ ] Pool de hilos para cada conexión; manejo de sesiones por cliente.
- [ ] Registro de clientes conectados para broadcast (login/logout/mensajes).
- [ ] JSON por línea (framing), validación y manejo de errores.
- [ ] Logging con SLF4J + Logback (o similar).
- [ ] Cierre ordenado de recursos (sockets, hilos, conexiones).

## 3. Backend — Persistencia (MySQL)
- [ ] Esquema en `db/schema.sql` (tablas, FKs, índices).
- [ ] Datos de ejemplo en `db/seed.sql`.
- [ ] Configurar JDBC y pool (HikariCP recomendado).
- [ ] DAOs/Repos: Users, Patients, Medics, Pharmacists, Medications, Prescriptions(+Items), Dispenses, Messages.
- [ ] Transacciones (crear receta con items, despacho con actualización de stock).
- [ ] Mapeos entidad↔DTO.

## 4. Backend — Lógica de negocio (Service)
- [ ] Autenticación (login) y cambio de contraseña (hash con BCrypt/PBKDF2).
- [ ] Gestión de usuarios (ADMIN): listar/crear/editar/eliminar, roles.
- [ ] Listados: Médicos, Farmacéuticos, Pacientes, Medicamentos.
- [ ] Prescripción: crear receta con items y validaciones (ej. stock, reglas).
- [ ] Despacho: registrar despacho, decrementar stock, validar estados.
- [ ] Histórico de recetas (por paciente/por médico).
- [ ] Dashboard (indicadores: total recetas, por estado, top medicamentos, etc.).
- [ ] Mensajería: persistir mensaje, marcar recibido.
- [ ] Notificaciones asíncronas: USER_LOGIN/USER_LOGOUT, MESSAGE_DELIVERED.
- [ ] Autorización por rol para cada operación.
- [ ] Validaciones exhaustivas y mensajes de error consistentes.

## 5. Frontend — Infraestructura y Proxy
- [ ] Proxy que implementa los contratos del Service y gestiona el socket.
- [ ] Hilo/escucha para notificaciones asíncronas y actualización de UI.
- [ ] Manejo de reconexión y visualización de estado de conexión.
- [ ] Manejo de errores y feedback al usuario (diálogos no bloqueantes).

## 6. Frontend — UI (MVC)
- [ ] Login y cambio de contraseña.
- [ ] Pantalla principal con área de “Usuarios activos” (actualización dinámica).
- [ ] Módulos:
  - [ ] Lista de Médicos (4%)
  - [ ] Lista de Farmacéuticos (6%)
  - [ ] Lista de Pacientes (6%)
  - [ ] Catálogo de medicamentos (6%)
  - [ ] Prescripción (10%)
  - [ ] Despacho (10%)
  - [ ] Histórico de recetas (6%)
  - [ ] Usuarios (10%) — administración (según rol)
  - [ ] Dashboard (20%) — indicadores y visualizaciones
- [ ] Mensajería:
  - [ ] Enviar mensaje a usuario activo (ventana emergente).
  - [ ] Recibir mensaje (seleccionar remitente y “Recibir”; ventana emergente).
- [ ] Validaciones de formularios y estados de carga.

## 7. Seguridad y calidad
- [ ] Hash de contraseñas (BCrypt/PBKDF2) y política de claves.
- [ ] Sanitizar entradas, validar tamaños y formatos.
- [ ] Manejo de sesiones y expiración (si aplica).
- [ ] Logs de auditoría (login/logout, creación/edición/elim. de entidades, despacho).
- [ ] Pruebas unitarias de Service y DAOs.
- [ ] Pruebas de integración: flujo login, prescripción, despacho, mensajería.
- [ ] Pruebas de concurrencia (varios clientes).
- [ ] Monitoreo básico (métricas del servidor, conexiones activas).

## 8. Protocolo de red (detallado)
- [ ] Documentar en `docs/protocolo.md`:
  - [ ] Esquema de cada `op`, `data` esperado, `response`, errores.
  - [ ] Ejemplos de request/response.
  - [ ] Notificaciones asíncronas y su payload.
  - [ ] Códigos de error y recomendaciones de reintento.

## 9. Entrega y documentación
- [ ] Completar README con instrucciones definitivas de build y run.
- [ ] Incluir script SQL (schema + seed) y nota de credenciales de prueba.
- [ ] Empaquetar como un único archivo comprimido con fuentes y scripts.
- [ ] Revisar que todas las validaciones y mensajes sean claros.
- [ ] Ensayo de defensa y demo (escenarios: login, notificaciones, chat, prescripción, despacho, dashboard, histórico).

## 10. Mapeo a la rúbrica (check rápido)
- [ ] 1. Login y cambio de clave (10%)
- [ ] 2. Prescripción (10%)
- [ ] 3. Despacho (10%)
- [ ] 4. Lista de Médicos (6%)
- [ ] 5. Lista de Farmacéuticos (6%)
- [ ] 6. Lista de Pacientes (6%)
- [ ] 7. Catálogo de medicamentos (6%)
- [ ] 8. Dashboard (Indicadores) (20%)
- [ ] 9. Histórico de recetas (6%)
- [ ] 10. Usuarios (10%)
- [ ] 11. Mostrar y recibir mensajes (10%)
