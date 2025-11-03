package org.example.Domain.Dtos.Mensaje;

import java.io.Serializable;
import java.util.List;

public class ListMensajeResponseDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<MensajeResponseDto> mensajes;

    public ListMensajeResponseDto() {}

    public ListMensajeResponseDto(List<MensajeResponseDto> mensajes) {
        this.mensajes = mensajes;
    }

    public List<MensajeResponseDto> getMensajes() {
        return mensajes;
    }

    public void setMensajes(List<MensajeResponseDto> mensajes) {
        this.mensajes = mensajes;
    }

    @Override
    public String toString() {
        return "ListMensajeResponseDto{" +
                "mensajes=" + mensajes +
                '}';
    }
}
