CREATE TABLE pacientes (
                           id BIGINT PRIMARY KEY AUTO_INCREMENT,
                           nombre VARCHAR(100) NOT NULL,
                           telefono VARCHAR(20),
                           fecha_nacimiento DATE,
                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                           INDEX idx_nombre (nombre),
                           INDEX idx_created_at (created_at),
                           INDEX idx_updated_at (updated_at)
);