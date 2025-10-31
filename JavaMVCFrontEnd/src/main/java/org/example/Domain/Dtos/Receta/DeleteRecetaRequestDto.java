package org.example.Domain.Dtos.Receta;

public class DeleteRecetaRequestDto {
    private int id;

    public DeleteRecetaRequestDto() {}

    public DeleteRecetaRequestDto(int id) {
        this.id = id;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
}

