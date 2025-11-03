package org.example.Domain.dtos.Mensaje;

import java.io.Serializable;

public class AddMensajeRequestDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String remitente;
    private String destinatario;
    private String contenido;

    public AddMensajeRequestDto() {}

    public AddMensajeRequestDto(String remitente, String destinatario, String contenido) {
        this.remitente = remitente;
        this.destinatario = destinatario;
        this.contenido = contenido;
    }

    public String getRemitente() {
        return remitente;
    }

    public void setRemitente(String remitente) {
        this.remitente = remitente;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }
}