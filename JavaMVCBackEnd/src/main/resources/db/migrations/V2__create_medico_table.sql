CREATE TABLE medico (
                        id BIGINT PRIMARY KEY,
                        especialidad VARCHAR(100) NOT NULL,
                        FOREIGN KEY (id) REFERENCES usuario(id) ON DELETE CASCADE
);
