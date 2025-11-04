package org.example.Domain.Dtos.Receta;

import org.example.Domain.Dtos.DetalleReceta.DetalleRecetaResponseDto;
import java.util.List;

public class RecetaResponseDto {
    private String nombrePaciente;
    private String nombreMedico;
    private String fechaRetiro;
    private int id;
    private int idPaciente;
    private int idMedico;
    private String fechaConfeccion;
    private List<DetalleRecetaResponseDto> detalles;
    private String estado;

    public RecetaResponseDto() {}

    public RecetaResponseDto(int id, int idPaciente, int idMedico,
                             String nombrePaciente, String nombreMedico,
                             String fechaConfeccion, String fechaRetiro,
                             String estado, List<DetalleRecetaResponseDto> detalles) {
        this.id = id;
        this.idPaciente = idPaciente;
        this.idMedico = idMedico;
        this.nombrePaciente = nombrePaciente;
        this.nombreMedico = nombreMedico;
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

    public List<DetalleRecetaResponseDto> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleRecetaResponseDto> detalles) { this.detalles = detalles; }

    public String getNombrePaciente() { return nombrePaciente; }
    public void setNombrePaciente(String nombrePaciente) { this.nombrePaciente = nombrePaciente; }
    public String getNombreMedico() { return nombreMedico; }
    public void setNombreMedico(String nombreMedico) { this.nombreMedico = nombreMedico; }
    public String getFechaRetiro() { return fechaRetiro; }
    public void setFechaRetiro(String fechaRetiro) { this.fechaRetiro = fechaRetiro; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
