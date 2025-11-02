CREATE TABLE medicos (
                         id BIGINT PRIMARY KEY,
                         especialidad VARCHAR(100) NOT NULL,
                         FOREIGN KEY (id) REFERENCES usuarios(id) ON DELETE CASCADE
);