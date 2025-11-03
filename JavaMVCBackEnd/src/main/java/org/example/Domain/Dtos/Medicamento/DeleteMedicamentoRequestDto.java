package org.example.Domain.Dtos.Medicamento;

public class DeleteMedicamentoRequestDto {
    private int id;

    public DeleteMedicamentoRequestDto() {
    }

    public DeleteMedicamentoRequestDto(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

// Revisar