package org.example.Domain.Dtos.DetalleReceta;

import java.util.List;

public class ListDetalleRecetaResponseDto {
    private List<DetalleRecetaResponseDto> detalles;

    public ListDetalleRecetaResponseDto() {}

    public ListDetalleRecetaResponseDto(List<DetalleRecetaResponseDto> detalles) {
        this.detalles = detalles;
    }

    public List<DetalleRecetaResponseDto> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleRecetaResponseDto> detalles) { this.detalles = detalles; }
}

