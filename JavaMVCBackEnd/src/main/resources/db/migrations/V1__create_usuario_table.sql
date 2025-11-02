CREATE TABLE usuarios (
                          id BIGINT PRIMARY KEY AUTO_INCREMENT,
                          nombre VARCHAR(100) NOT NULL UNIQUE,
                          clave VARCHAR(255) NOT NULL,
                          salt VARCHAR(255) NOT NULL,
                          is_active BOOLEAN DEFAULT FALSE,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                          INDEX idx_nombre (nombre),
                          INDEX idx_is_active (is_active)
);