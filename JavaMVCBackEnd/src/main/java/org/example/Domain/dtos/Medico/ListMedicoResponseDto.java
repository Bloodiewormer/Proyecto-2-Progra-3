package org.example.Domain.dtos.Medico;

import java.util.List;

public class ListMedicoResponseDto {
    private List<MedicoResponseDto> medicos;

    public ListMedicoResponseDto() {}

    public ListMedicoResponseDto(List<MedicoResponseDto> medicos) {
        this.medicos = medicos;
    }

    public List<MedicoResponseDto> getMedicos() { return medicos; }
    public void setMedicos(List<MedicoResponseDto> medicos) { this.medicos = medicos; }
}
