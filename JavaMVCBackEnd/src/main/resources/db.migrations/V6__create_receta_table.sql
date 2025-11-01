CREATE TABLE receta (
                        id BIGINT PRIMARY KEY AUTO_INCREMENT,
                        id_paciente BIGINT NOT NULL,
                        id_medico BIGINT NOT NULL,
                        fecha_confeccion DATE NOT NULL,
                        FOREIGN KEY (id_paciente) REFERENCES paciente(id) ON DELETE CASCADE,
                        FOREIGN KEY (id_medico) REFERENCES medico(id) ON DELETE CASCADE
);
