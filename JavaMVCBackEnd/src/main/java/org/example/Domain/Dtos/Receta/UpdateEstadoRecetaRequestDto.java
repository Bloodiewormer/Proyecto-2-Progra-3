package org.example.Domain.Dtos.Receta;

public class UpdateEstadoRecetaRequestDto {
    private int id;
    private String estado;

    public UpdateEstadoRecetaRequestDto() {}

    public UpdateEstadoRecetaRequestDto(int id, String estado) {
        this.id = id;
        this.estado = estado;
    }

    public int getId() { return id; }
    public String getEstado() { return estado; }
}