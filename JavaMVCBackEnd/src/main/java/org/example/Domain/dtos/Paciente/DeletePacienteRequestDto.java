package org.example.Domain.dtos.Paciente;

public class DeletePacienteRequestDto {
    private int id;

    public DeletePacienteRequestDto() {}

    public DeletePacienteRequestDto(int id) {
        this.id = id;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
}

