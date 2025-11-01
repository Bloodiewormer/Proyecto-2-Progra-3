CREATE TABLE usuario (
                         id BIGINT PRIMARY KEY AUTO_INCREMENT,
                         nombre VARCHAR(100) NOT NULL UNIQUE,
                         clave VARCHAR(255) NOT NULL
);
