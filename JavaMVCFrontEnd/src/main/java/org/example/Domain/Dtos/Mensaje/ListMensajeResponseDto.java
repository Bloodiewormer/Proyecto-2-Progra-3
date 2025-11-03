package org.example.Domain.Dtos.Mensaje;

import java.io.Serializable;
import java.util.List;

public class ListMensajeResponseDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String usuario1;
    private String usuario2;
    private List<MensajeResponseDto> mensajes;

    public ListMensajeResponseDto() {}

    public ListMensajeResponseDto(String usuario1, String usuario2) {
        this.usuario1 = usuario1;
        this.usuario2 = usuario2;
    }

    public ListMensajeResponseDto(List<MensajeResponseDto> mensajes) {
        this.mensajes = mensajes;
    }

    public String getUsuario1() {
        return usuario1;
    }

    public void setUsuario1(String usuario1) {
        this.usuario1 = usuario1;
    }

    public String getUsuario2() {
        return usuario2;
    }

    public void setUsuario2(String usuario2) {
        this.usuario2 = usuario2;
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
                "usuario1='" + usuario1 + '\'' +
                ", usuario2='" + usuario2 + '\'' +
                ", mensajes=" + mensajes +
                '}';
    }
}

