package org.example.Domain.Dtos.Mensaje;

import java.io.Serializable;

public class UpdateMensajeRequestDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private boolean leido;

    public UpdateMensajeRequestDto() {}

    public UpdateMensajeRequestDto(int id, boolean leido) {
        validateId(id);
        this.id = id;
        this.leido = leido;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        validateId(id);
        this.id = id;
    }

    public boolean isLeido() {
        return leido;
    }

    public void setLeido(boolean leido) {
        this.leido = leido;
    }

    // Método de validación
    private void validateId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID del mensaje debe ser mayor a 0");
        }
    }

    @Override
    public String toString() {
        return "UpdateMensajeRequestDto{" +
                "id=" + id +
                ", leido=" + leido +
                '}';
    }
}
