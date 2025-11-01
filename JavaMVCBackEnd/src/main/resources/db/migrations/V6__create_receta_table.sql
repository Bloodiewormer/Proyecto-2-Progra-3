CREATE TABLE recetas (
                         id BIGINT PRIMARY KEY AUTO_INCREMENT,
                         codigo VARCHAR(50) UNIQUE,
                         id_paciente BIGINT NOT NULL,
                         id_medico BIGINT NOT NULL,
                         fecha_confeccion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         fecha_retiro TIMESTAMP NULL,
                         estado VARCHAR(20) DEFAULT 'PENDIENTE',
                         FOREIGN KEY (id_paciente) REFERENCES pacientes(id) ON DELETE CASCADE,
                         FOREIGN KEY (id_medico) REFERENCES medicos(id) ON DELETE CASCADE,
                         INDEX idx_estado (estado),
                         INDEX idx_paciente (id_paciente),
                         INDEX idx_medico (id_medico),
                         INDEX idx_fecha_confeccion (fecha_confeccion)
);