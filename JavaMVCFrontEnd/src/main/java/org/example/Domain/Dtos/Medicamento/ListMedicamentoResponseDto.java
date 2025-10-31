package org.example.Domain.Dtos.Medicamento;

import java.util.List;

public class ListMedicamentoResponseDto {
    private List<MedicamentoResponseDto> medicamentos;

    public ListMedicamentoResponseDto() {}

    public ListMedicamentoResponseDto(List<MedicamentoResponseDto> medicamentos) {
        this.medicamentos = medicamentos;
    }

    public List<MedicamentoResponseDto> getMedicamentos() { return medicamentos; }
    public void setMedicamentos(List<MedicamentoResponseDto> medicamentos) { this.medicamentos = medicamentos; }
}

