package org.example.Domain.Dtos.Receta;

import org.example.Domain.Dtos.DetalleReceta.UpdateDetalleRecetaRequestDto;
import java.util.List;

public class UpdateRecetaRequestDto {
    private int id;
    private int idPaciente;
    private int idMedico;
    private String fechaConfeccion;
    private String fechaRetiro;
    private String estado;
    private List<UpdateDetalleRecetaRequestDto> detalles;

    public UpdateRecetaRequestDto() {}

    public UpdateRecetaRequestDto(int id, int idPaciente, int idMedico, String fechaConfeccion, String fechaRetiro, String estado, List<UpdateDetalleRecetaRequestDto> detalles) {
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

    public List<UpdateDetalleRecetaRequestDto> getDetalles() { return detalles; }
    public void setDetalles(List<UpdateDetalleRecetaRequestDto> detalles) { this.detalles = detalles; }
}



