package org.example.Domain.Dtos.Mensaje;

import java.io.Serializable;

public class EstadoUsuarioDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String username;
    private boolean activo;

    public EstadoUsuarioDto() {}

    public EstadoUsuarioDto(String username, boolean activo) {
        validateUsername(username);
        this.username = username;
        this.activo = activo;
    }

    // Getters y Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        validateUsername(username);
        this.username = username;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    // Método de validación
    private void validateUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("El username no puede estar vacío");
        }
    }

    @Override
    public String toString() {
        return "EstadoUsuarioDto{" +
                "username='" + username + '\'' +
                ", activo=" + activo +
                '}';
    }
}

