package org.example.domain_layer;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.Objects;

@XmlRootElement(name = "medicamento")
@XmlAccessorType(XmlAccessType.FIELD)
public class Medicamento {

    @XmlElement
    private int codigo;
    @XmlElement
    private String nombre;
    @XmlElement
    private String presentacion;

    public Medicamento() {}

    public Medicamento(int codigo, String nombre, String presentacion) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.presentacion = presentacion;
    }

    public int getCodigo() { return codigo; }
    public void setCodigo(int codigo) { this.codigo = codigo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getPresentacion() { return presentacion; }
    public void setPresentacion(String presentacion) { this.presentacion = presentacion; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Medicamento)) return false;
        Medicamento that = (Medicamento) o;
        return codigo == that.codigo;
    }

    @Override
    public int hashCode() { return Objects.hash(codigo); }

    @Override
    public String toString() {
        return nombre + " (" + presentacion + ")";
    }
}