CREATE TABLE pacientes (
                           id BIGINT PRIMARY KEY AUTO_INCREMENT,
                           nombre VARCHAR(100) NOT NULL,
                           telefono VARCHAR(20),
                           fecha_nacimiento DATE,
                           INDEX idx_nombre (nombre)
);