-- Insertar Administrador
INSERT INTO usuarios (nombre, clave, salt, is_active) VALUES
    ('admin', 'FtlHkbLLlDy7K+cBxzGfEo4fLEe7z8Kq3tJYvRqZE8Q=', 'nXZ7xH2KvL8pQ9mN3wR5tY', TRUE);
INSERT INTO administradores (id) VALUES (1);

-- Insertar Médicos
INSERT INTO usuarios (nombre, clave, salt, is_active) VALUES
                                                          ('dr_garcia', 'hash123', 'salt123', FALSE),
                                                          ('dra_martinez', 'hash456', 'salt456', FALSE),
                                                          ('dr_lopez', 'hash789', 'salt789', FALSE);

INSERT INTO medicos (id, especialidad) VALUES
                                           (2, 'Cardiología'),
                                           (3, 'Pediatría'),
                                           (4, 'Medicina General');

-- Insertar Farmacéuticos
INSERT INTO usuarios (nombre, clave, salt, is_active) VALUES
                                                          ('farm_rodriguez', 'hash111', 'salt111', FALSE),
                                                          ('farm_hernandez', 'hash222', 'salt222', FALSE);

INSERT INTO farmaceutas (id) VALUES (5), (6);

-- Insertar Pacientes
INSERT INTO pacientes (nombre, telefono, fecha_nacimiento) VALUES
                                                               ('Juan Pérez', '88881111', '1985-05-15'),
                                                               ('María González', '88882222', '1990-08-22'),
                                                               ('Carlos Ramírez', '88883333', '1978-12-03'),
                                                               ('Ana Mora', '88884444', '2000-03-17'),
                                                               ('Luis Castro', '88885555', '1995-11-28');

-- Insertar Medicamentos
INSERT INTO medicamentos (nombre, presentacion, stock, precio, is_active) VALUES
                                                                              ('Paracetamol', 'Tabletas 500mg x 20', 150, 2500.00, TRUE),
                                                                              ('Ibuprofeno', 'Cápsulas 400mg x 30', 200, 3500.00, TRUE),
                                                                              ('Amoxicilina', 'Suspensión 250mg/5ml', 80, 5500.00, TRUE),
                                                                              ('Omeprazol', 'Cápsulas 20mg x 14', 120, 4200.00, TRUE),
                                                                              ('Loratadina', 'Tabletas 10mg x 10', 180, 2800.00, TRUE),
                                                                              ('Salbutamol', 'Inhalador 100mcg', 60, 8500.00, TRUE),
                                                                              ('Metformina', 'Tabletas 850mg x 30', 100, 3200.00, TRUE),
                                                                              ('Atorvastatina', 'Tabletas 20mg x 30', 90, 6500.00, TRUE),
                                                                              ('Losartán', 'Tabletas 50mg x 30', 110, 4800.00, TRUE),
                                                                              ('Diclofenaco', 'Gel 1% tubo 30g', 75, 3800.00, TRUE);

-- Insertar Recetas de ejemplo
INSERT INTO recetas (codigo, id_paciente, id_medico, fecha_confeccion, fecha_retiro, estado) VALUES
                                                                                                 ('RX-2025-001', 1, 2, '2025-01-15 10:30:00', '2025-02-14 23:59:59', 'PENDIENTE'),
                                                                                                 ('RX-2025-002', 2, 3, '2025-01-16 14:20:00', '2025-02-15 23:59:59', 'PENDIENTE'),
                                                                                                 ('RX-2025-003', 3, 4, '2025-01-17 09:15:00', '2025-02-16 23:59:59', 'DESPACHADO'),
                                                                                                 ('RX-2025-004', 4, 2, '2025-01-18 11:45:00', '2025-02-17 23:59:59', 'PENDIENTE'),
                                                                                                 ('RX-2025-005', 5, 3, '2025-01-19 16:00:00', '2025-02-18 23:59:59', 'CANCELADO');

-- Insertar Detalles de Recetas
INSERT INTO detalle_recetas (id_receta, id_medicamento, cantidad, indicaciones, dias) VALUES
                                                                                          (1, 1, 1, 'Tomar 1 tableta cada 8 horas', 10),
                                                                                          (1, 3, 1, 'Tomar 5ml cada 12 horas', 7),
                                                                                          (2, 2, 1, 'Tomar 1 cápsula cada 8 horas con alimentos', 5),
                                                                                          (2, 5, 1, 'Tomar 1 tableta cada 24 horas', 10),
                                                                                          (3, 4, 1, 'Tomar 1 cápsula en ayunas', 14),
                                                                                          (3, 7, 1, 'Tomar 1 tableta con el desayuno', 30),
                                                                                          (4, 6, 1, 'Aplicar 2 inhalaciones cada 6 horas si hay dificultad respiratoria', 30),
                                                                                          (5, 8, 1, 'Tomar 1 tableta antes de dormir', 30);

-- Insertar Despacho de ejemplo
INSERT INTO despachos (id_receta, id_farmaceuta, fecha_despacho, observaciones) VALUES
    (3, 5, '2025-01-20 10:30:00', 'Medicamentos entregados completos. Paciente recibió indicaciones.');

-- Insertar Mensajes de ejemplo
INSERT INTO messages (sender_id, recipient_id, message, status) VALUES
                                                                    (2, 5, 'Favor verificar disponibilidad de Amoxicilina para receta RX-2025-001', 'SENT'),
                                                                    (5, 2, 'Amoxicilina disponible. Stock: 80 unidades', 'SENT'),
                                                                    (1, 2, 'Recordatorio: Actualizar estadísticas mensuales', 'SENT');