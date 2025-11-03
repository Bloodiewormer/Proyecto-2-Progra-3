package org.example.Domain.Dtos.Mensaje;

import java.io.Serializable;

public class DeleteMensajeRequestDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;

    public DeleteMensajeRequestDto() {}

    public DeleteMensajeRequestDto(int id) {
        validateId(id);
        this.id = id;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        validateId(id);
        this.id = id;
    }

    // Método de validación
    private void validateId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID del mensaje debe ser mayor a 0");
        }
    }

    @Override
    public String toString() {
        return "DeleteMensajeRequestDto{id=" + id + '}';
    }
}

