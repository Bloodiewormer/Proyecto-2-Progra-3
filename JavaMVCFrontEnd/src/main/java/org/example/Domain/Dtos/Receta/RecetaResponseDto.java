package org.example.Domain.Dtos.Receta;

import org.example.Domain.Dtos.DetalleReceta.DetalleRecetaResponseDto;
import java.util.List;

public class RecetaResponseDto {
    private int id;
    private int idPaciente;
    private int idMedico;
    private String fechaConfeccion;
    private String fechaRetiro;
    private String estado;
    private List<DetalleRecetaResponseDto> detalles;

    public RecetaResponseDto() {}

    public RecetaResponseDto(int id, int idPaciente, int idMedico, String fechaConfeccion, String fechaRetiro, String estado, List<DetalleRecetaResponseDto> detalles) {
        this.id = id;
        this.idPaciente = idPaciente;
        this.idMedico = idMedico;
        this.fechaConfeccion = fechaConfeccion;
        this.fechaRetiro = fechaRetiro;
        this.estado = estado;
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

    public String getFechaRetiro() { return fechaRetiro; }
    public void setFechaRetiro(String fechaRetiro) { this.fechaRetiro = fechaRetiro; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public List<DetalleRecetaResponseDto> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleRecetaResponseDto> detalles) { this.detalles = detalles; }
}



