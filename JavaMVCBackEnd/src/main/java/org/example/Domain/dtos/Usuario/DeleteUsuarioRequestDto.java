package org.example.Domain.dtos.Usuario;

public class DeleteUsuarioRequestDto {
    private int id;

    public DeleteUsuarioRequestDto() {}

    public DeleteUsuarioRequestDto(int id) {
        this.id = id;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
}

