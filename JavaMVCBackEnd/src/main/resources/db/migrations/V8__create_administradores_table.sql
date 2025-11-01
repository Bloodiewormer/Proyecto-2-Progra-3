CREATE TABLE administradores (
                                 id BIGINT PRIMARY KEY,
                                 FOREIGN KEY (id) REFERENCES usuarios(id) ON DELETE CASCADE
);