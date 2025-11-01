package org.example.Domain.dtos.Receta;

import org.example.Domain.dtos.DetalleReceta.DetalleRecetaResponseDto;
import java.util.List;

public class RecetaResponseDto {
    private int id;
    private int idPaciente;
    private int idMedico;
    private String fechaConfeccion;
    private List<DetalleRecetaResponseDto> detalles;

    public RecetaResponseDto() {}

    public RecetaResponseDto(int id, int idPaciente, int idMedico, String fechaConfeccion, List<DetalleRecetaResponseDto> detalles) {
        this.id = id;
        this.idPaciente = idPaciente;
        this.idMedico = idMedico;
        this.fechaConfeccion = fechaConfeccion;
        this.detalles = detalles;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdPaciente() { return idPaciente; }
    public void setIdPaciente(int idPaciente) { this.idPaciente = idPaciente; }

    public int getIdMedico() { return idMedico; }
    public void setIdMedico(int idMedico) { this.idMedico = idMedico; }

    public String getFechaConfeccion() { return fechaConfeccion; }
    public void setFechaConfeccion(String fechaConfeccion) { this.fechaConfeccion = fechaConfeccion; }

    public List<DetalleRecetaResponseDto> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleRecetaResponseDto> detalles) { this.detalles = detalles; }
}
