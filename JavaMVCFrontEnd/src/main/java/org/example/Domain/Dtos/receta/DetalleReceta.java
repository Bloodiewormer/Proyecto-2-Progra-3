package org.example.Domain.Dtos.receta;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class DetalleReceta {

    @XmlElement
    private Medicamento medicamento;
    @XmlElement
    private int cantidad;
    @XmlElement
    private String indicaciones;
    @XmlElement
    private int dias;

    @SuppressWarnings("unused")
    public DetalleReceta() {}
    @SuppressWarnings( "unused")
    public DetalleReceta(Medicamento medicamento, int cantidad, String indicaciones, int dias) {
        this.medicamento = medicamento;
        this.cantidad = cantidad;
        this.indicaciones = indicaciones;
        this.dias = dias;
    }

    public Medicamento getMedicamento() { return medicamento; }
    public void setMedicamento(Medicamento medicamento) { this.medicamento = medicamento; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public String getIndicaciones() { return indicaciones; }
    public void setIndicaciones(String indicaciones) { this.indicaciones = indicaciones; }

    public int getDias() { return dias; }
    public void setDias(int dias) { this.dias = dias; }
}