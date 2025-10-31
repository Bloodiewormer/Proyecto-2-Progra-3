package org.example.Domain.Administrador;

public class DeleteAdministradorRequestDto {
    private int id;

    public DeleteAdministradorRequestDto() {}

    public DeleteAdministradorRequestDto(int id) {
        this.id = id;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
}

