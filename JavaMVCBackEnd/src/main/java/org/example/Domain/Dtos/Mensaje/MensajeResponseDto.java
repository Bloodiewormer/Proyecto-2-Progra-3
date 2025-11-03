package org.example.Domain.Dtos.Mensaje;

import java.io.Serializable;

public class MensajeResponseDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String remitente;
    private String destinatario;
    private String contenido;
    private String estado;
    private String fechaHora;      // ✅ Cambiar a String
    private String leido;          // ✅ Cambiar a String

    public MensajeResponseDto() {}

    public MensajeResponseDto(Long id, String remitente, String destinatario,
                              String contenido, String estado,
                              String fechaHora, String leido) {
        this.id = id;
        this.remitente = remitente;
        this.destinatario = destinatario;
        this.contenido = contenido;
        this.estado = estado;
        this.fechaHora = fechaHora;
        this.leido = leido;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getLeido() {
        return leido;
    }

    public void setLeido(String leido) {
        this.leido = leido;
    }
}