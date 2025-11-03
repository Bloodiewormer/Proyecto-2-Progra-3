package org.example.Domain.Dtos.Mensaje;

import java.io.Serializable;
import java.time.LocalDateTime;

public class MensajeResponseDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String remitente;
    private String destinatario;
    private String contenido;
    private LocalDateTime fechaHora;
    private boolean leido;

    public MensajeResponseDto() {
        this.fechaHora = LocalDateTime.now();
        this.leido = false;
    }

    public MensajeResponseDto(int id, String remitente, String destinatario,
                              String contenido, LocalDateTime fechaHora, boolean leido) {
        validateId(id);
        validateRemitente(remitente);
        validateDestinatario(destinatario);
        validateContenido(contenido);
        validateFechaHora(fechaHora);

        this.id = id;
        this.remitente = remitente;
        this.destinatario = destinatario;
        this.contenido = contenido;
        this.fechaHora = fechaHora;
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

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        validateFechaHora(fechaHora);
        this.fechaHora = fechaHora;
    }

    public boolean isLeido() {
        return leido;
    }

    public void setLeido(boolean leido) {
        this.leido = leido;
    }

    // Métodos de validación
    private void validateId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID del mensaje debe ser mayor a 0");
        }
    }

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
    }

    private void validateFechaHora(LocalDateTime fechaHora) {
        if (fechaHora == null) {
            throw new IllegalArgumentException("La fecha y hora no pueden ser nulas");
        }
    }

    @Override
    public String toString() {
        return "MensajeResponseDto{" +
                "id=" + id +
                ", remitente='" + remitente + '\'' +
                ", destinatario='" + destinatario + '\'' +
                ", contenido='" + contenido + '\'' +
                ", fechaHora=" + fechaHora +
                ", leido=" + leido +
                '}';
    }
}
