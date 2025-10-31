package org.example.Domain.DetalleReceta;

public class DeleteDetalleRecetaRequestDto {
    private int id;

    public DeleteDetalleRecetaRequestDto() {}

    public DeleteDetalleRecetaRequestDto(int id) {
        this.id = id;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
}
