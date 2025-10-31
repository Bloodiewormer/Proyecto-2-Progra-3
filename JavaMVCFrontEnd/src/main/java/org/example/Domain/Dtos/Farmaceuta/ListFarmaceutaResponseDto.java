package org.example.Domain.Dtos.Farmaceuta;

import java.util.List;

public class ListFarmaceutaResponseDto {
    private List<FarmaceutaResponseDto> farmaceutas;

    public ListFarmaceutaResponseDto() {}

    public ListFarmaceutaResponseDto(List<FarmaceutaResponseDto> farmaceutas) {
        this.farmaceutas = farmaceutas;
    }

    public List<FarmaceutaResponseDto> getFarmaceutas() {
        return farmaceutas;
    }

    public void setFarmaceutas(List<FarmaceutaResponseDto> farmaceutas) {
        this.farmaceutas = farmaceutas;
    }
}

