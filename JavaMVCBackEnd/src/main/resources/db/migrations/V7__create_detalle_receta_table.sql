CREATE TABLE detalle_recetas (
                                 id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                 id_receta BIGINT NOT NULL,
                                 id_medicamento BIGINT NOT NULL,
                                 cantidad INT NOT NULL,
                                 indicaciones VARCHAR(500),
                                 dias INT DEFAULT 30,
                                 FOREIGN KEY (id_receta) REFERENCES recetas(id) ON DELETE CASCADE,
                                 FOREIGN KEY (id_medicamento) REFERENCES medicamentos(id) ON DELETE CASCADE,
                                 INDEX idx_receta (id_receta),
                                 INDEX idx_medicamento (id_medicamento)
);