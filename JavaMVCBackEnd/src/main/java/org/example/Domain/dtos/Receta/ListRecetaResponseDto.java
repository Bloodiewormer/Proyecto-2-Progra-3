package org.example.Domain.dtos.Receta;

import java.util.List;

public class ListRecetaResponseDto {
    private List<RecetaResponseDto> recetas;

    public ListRecetaResponseDto() {}

    public ListRecetaResponseDto(List<RecetaResponseDto> recetas) {
        this.recetas = recetas;
    }

    public List<RecetaResponseDto> getRecetas() { return recetas; }
    public void setRecetas(List<RecetaResponseDto> recetas) { this.recetas = recetas; }
}

