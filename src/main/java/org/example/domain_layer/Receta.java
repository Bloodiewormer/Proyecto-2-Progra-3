package org.example.domain_layer;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import org.example.utilities.EstadoReceta;

import java.time.LocalDate;
import java.util.Date;

import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "receta")
@XmlAccessorType(XmlAccessType.FIELD)
public class Receta {


    @XmlElement
    private int id;
    @XmlElement
    private int idPaciente;
    @XmlElement
    private int idMedico;
    @XmlElement
    private Date fechaConfeccion;
    @XmlElement
    private Date fechaRetiro;
    @XmlElement
    private String estado;

    @XmlElementWrapper(name = "detalles")
    @XmlElement(name = "detalle")
    private List<DetalleReceta> detalles = new ArrayList<>();

    public Receta() {}

    public Receta(int id, int idPaciente, int idMedico, Date fechaConfeccion, Date fechaRetiro, String estado) {
        this.id = id;
        this.idPaciente = idPaciente;
        this.idMedico = idMedico;
        this.fechaConfeccion = fechaConfeccion;
        this.fechaRetiro = fechaRetiro;
        this.estado = estado;

    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdPaciente() { return idPaciente; }
    public void setIdPaciente(int idPaciente) { this.idPaciente = idPaciente; }

    public int getIdMedico() { return idMedico; }
    public void setIdMedico(int idMedico) { this.idMedico = idMedico; }

    public Date getFechaConfeccion() { return fechaConfeccion; }
    public void setFechaConfeccion(Date fechaConfeccion) { this.fechaConfeccion = fechaConfeccion; }

    public Date getFechaRetiro() { return fechaRetiro; }
    public void setFechaRetiro(Date fechaRetiro) { this.fechaRetiro = fechaRetiro; }

    public String getEstado() { return estado; }
    public void setEstado(EstadoReceta estado) {  this.estado = estado.toString(); }

    public List<DetalleReceta> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleReceta> detalles) { this.detalles = detalles; }
}