package org.example.Domain.Dtos.Mensaje;

import java.io.Serializable;

public class AddMensajeRequestDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final int MAX_CONTENIDO_LENGTH = 500;

    private String remitente;
    private String destinatario;
    private String contenido;

    public AddMensajeRequestDto() {}

    public AddMensajeRequestDto(String remitente, String destinatario, String contenido) {
        validateRemitente(remitente);
        validateDestinatario(destinatario);
        validateContenido(contenido);

        this.remitente = remitente;
        this.destinatario = destinatario;
        this.contenido = contenido;
    }

    // Getters y Setters
    public String getRemitente() {
        return remitente;
    }

    public void setRemitente(String remitente) {
        validateRemitente(remitente);
        this.remitente = remitente;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        validateDestinatario(destinatario);
        this.destinatario = destinatario;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        validateContenido(contenido);
        this.contenido = contenido;
    }

    // Métodos de validación
    private void validateRemitente(String remitente) {
        if (remitente == null || remitente.trim().isEmpty()) {
            throw new IllegalArgumentException("El remitente no puede estar vacío");
        }
    }

    private void validateDestinatario(String destinatario) {
        if (destinatario == null || destinatario.trim().isEmpty()) {
            throw new IllegalArgumentException("El destinatario no puede estar vacío");
        }
    }

    private void validateContenido(String contenido) {
        if (contenido == null || contenido.trim().isEmpty()) {
            throw new IllegalArgumentException("El contenido del mensaje no puede estar vacío");
        }
        if (contenido.length() > MAX_CONTENIDO_LENGTH) {
            throw new IllegalArgumentException(
                    "El contenido del mensaje no puede exceder " + MAX_CONTENIDO_LENGTH + " caracteres"
            );
        }
    }

    @Override
    public String toString() {
        return "AddMensajeRequestDto{" +
                "remitente='" + remitente + '\'' +
                ", destinatario='" + destinatario + '\'' +
                ", contenido='" + contenido + '\'' +
                '}';
    }
}
