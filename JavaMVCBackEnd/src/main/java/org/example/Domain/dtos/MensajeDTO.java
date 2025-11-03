package org.example.Domain.dtos;

import java.time.LocalDateTime;

public class MensajeDTO {
    private String id;
    private String remitenteId;
    private String destinatarioId;
    private String contenido;
    private LocalDateTime fechaEnvio;
    private boolean leido;

    // Constructores, Getters y Setters
    public MensajeDTO() {}

    public MensajeDTO(String remitenteId, String destinatarioId, String contenido) {
        this.remitenteId = remitenteId;
        this.destinatarioId = destinatarioId;
        this.contenido = contenido;
        this.fechaEnvio = LocalDateTime.now();
    }

    // Getters y Setters...
}
