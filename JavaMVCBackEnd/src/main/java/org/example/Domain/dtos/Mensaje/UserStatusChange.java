package org.example.Domain.dtos.Mensaje;

import java.io.Serializable;

/**
 * Notificación de cambio de estado de usuario
 * Se envía cuando un usuario se conecta o desconecta
 */
public class UserStatusChange implements Serializable {
    private static final long serialVersionUID = 1L;

    private String username;
    private boolean active;

    public UserStatusChange() {}

    public UserStatusChange(String username, boolean active) {
        this.username = username;
        this.active = active;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "UserStatusChange{" +
                "username='" + username + '\'' +
                ", active=" + active +
                '}';
    }
}