package org.example.Domain.Dtos.Medico;

public class DeleteMedicoRequestDto {
    private int id;

    public DeleteMedicoRequestDto() {}

    public DeleteMedicoRequestDto(int id) {
        this.id = id;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
}
