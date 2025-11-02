CREATE TABLE farmaceutas (
                             id BIGINT PRIMARY KEY,
                             FOREIGN KEY (id) REFERENCES usuarios(id) ON DELETE CASCADE
);