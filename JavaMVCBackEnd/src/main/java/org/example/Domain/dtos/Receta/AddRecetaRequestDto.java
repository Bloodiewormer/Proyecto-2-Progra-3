package org.example.Domain.dtos.Receta;

import org.example.Domain.dtos.DetalleReceta.AddDetalleRecetaRequestDto;
import java.util.List;

public class AddRecetaRequestDto {
    private int idPaciente;
    private int idMedico;
    private String fechaConfeccion;
    private List<AddDetalleRecetaRequestDto> detalles;

    public AddRecetaRequestDto() {}

    public AddRecetaRequestDto(int idPaciente, int idMedico, String fechaConfeccion, List<AddDetalleRecetaRequestDto> detalles) {
        this.idPaciente = idPaciente;
        this.idMedico = idMedico;
        this.fechaConfeccion = fechaConfeccion;
        this.detalles = detalles;
    }

    public int getIdPaciente() { return idPaciente; }
    public void setIdPaciente(int idPaciente) { this.idPaciente = idPaciente; }

    public int getIdMedico() { return idMedico; }
    public void setIdMedico(int idMedico) { this.idMedico = idMedico; }

    public String getFechaConfeccion() { return fechaConfeccion; }
    public void setFechaConfeccion(String fechaConfeccion) { this.fechaConfeccion = fechaConfeccion; }

    public List<AddDetalleRecetaRequestDto> getDetalles() { return detalles; }
    public void setDetalles(List<AddDetalleRecetaRequestDto> detalles) { this.detalles = detalles; }
}
