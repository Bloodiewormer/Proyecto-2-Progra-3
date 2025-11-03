package org.example.Domain.models;

import java.time.LocalDateTime;

public class Mensaje {
    private String id;
    private String remitenteId;
    private String destinatarioId;
    private String contenido;
    private LocalDateTime fechaEnvio;
    private boolean leido;

    public Mensaje(String remitenteId, String destinatarioId, String contenido) {
        this.id = java.util.UUID.randomUUID().toString();
        this.remitenteId = remitenteId;
        this.destinatarioId = destinatarioId;
        this.contenido = contenido;
        this.fechaEnvio = LocalDateTime.now();
        this.leido = false;
    }

    // Getters y Setters
    public String getId() { return id; }
    public String getRemitenteId() { return remitenteId; }
    public String getDestinatarioId() { return destinatarioId; }
    public String getContenido() { return contenido; }
    public LocalDateTime getFechaEnvio() { return fechaEnvio; }
    public boolean isLeido() { return leido; }
    public void marcarComoLeido() { this.leido = true; }
}
