CREATE TABLE farmaceuta (
                            id BIGINT PRIMARY KEY,
                            FOREIGN KEY (id) REFERENCES usuario(id) ON DELETE CASCADE
);
