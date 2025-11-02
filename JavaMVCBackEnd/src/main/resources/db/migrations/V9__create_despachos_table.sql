CREATE TABLE despachos (
                           id BIGINT PRIMARY KEY AUTO_INCREMENT,
                           id_receta BIGINT NOT NULL UNIQUE,
                           id_farmaceuta BIGINT NOT NULL,
                           fecha_despacho TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           observaciones VARCHAR(500),
                           FOREIGN KEY (id_receta) REFERENCES recetas(id) ON DELETE CASCADE,
                           FOREIGN KEY (id_farmaceuta) REFERENCES farmaceutas(id) ON DELETE CASCADE,
                           INDEX idx_receta (id_receta),
                           INDEX idx_farmaceuta (id_farmaceuta),
                           INDEX idx_fecha_despacho (fecha_despacho)
);