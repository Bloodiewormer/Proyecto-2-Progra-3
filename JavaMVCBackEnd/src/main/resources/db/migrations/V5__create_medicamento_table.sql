CREATE TABLE medicamentos (
                              id BIGINT PRIMARY KEY AUTO_INCREMENT,
                              nombre VARCHAR(100) NOT NULL,
                              presentacion VARCHAR(100) NOT NULL,
                              stock INT DEFAULT 0,
                              precio DECIMAL(10,2) DEFAULT 0.00,
                              is_active BOOLEAN DEFAULT TRUE,
                              INDEX idx_nombre (nombre),
                              INDEX idx_is_active (is_active)
);