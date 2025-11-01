CREATE TABLE paciente (
                          id BIGINT PRIMARY KEY AUTO_INCREMENT,
                          nombre VARCHAR(100) NOT NULL,
                          telefono VARCHAR(20),
                          fecha_nacimiento DATE
);
