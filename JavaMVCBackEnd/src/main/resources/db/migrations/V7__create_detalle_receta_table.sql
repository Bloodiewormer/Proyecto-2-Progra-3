CREATE TABLE detalle_receta (
                                id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                id_receta BIGINT NOT NULL,
                                id_medicamento BIGINT NOT NULL,
                                cantidad INT NOT NULL,
                                indicaciones VARCHAR(255),
                                dias INT,
                                FOREIGN KEY (id_receta) REFERENCES receta(id) ON DELETE CASCADE,
                                FOREIGN KEY (id_medicamento) REFERENCES medicamento(id) ON DELETE CASCADE
);
