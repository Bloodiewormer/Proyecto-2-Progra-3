package org.example.Domain.dtos.Paciente;

import java.util.List;

public class ListPacienteResponseDto {
    private List<PacienteResponseDto> pacientes;

    public ListPacienteResponseDto() {}

    public ListPacienteResponseDto(List<PacienteResponseDto> pacientes) {
        this.pacientes = pacientes;
    }

    public List<PacienteResponseDto> getPacientes() { return pacientes; }
    public void setPacientes(List<PacienteResponseDto> pacientes) { this.pacientes = pacientes; }
}

