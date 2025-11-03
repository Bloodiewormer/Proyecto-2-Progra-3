package org.example.Domain.Dtos.Mensaje;

import java.io.Serializable;

/**
 * Mensaje de notificación genérico del servidor de mensajería
 */
public class NotificationMessage implements Serializable {
    private static final long serialVersionUID = 1L;

    private String type;
    private Object data;

    public NotificationMessage() {}

    public NotificationMessage(String type, Object data) {
        this.type = type;
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "NotificationMessage{" +
                "type='" + type + '\'' +
                ", data=" + data +
                '}';
    }
}