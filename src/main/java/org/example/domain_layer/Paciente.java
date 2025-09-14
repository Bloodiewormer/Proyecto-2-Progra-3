package org.example.domain_layer;

import java.util.Date;

import jakarta.xml.bind.annotation.*;

@XmlAccessorType (XmlAccessType.FIELD)
@XmlRootElement
@XmlSeeAlso({Usuario.class})
public class Paciente extends Persona {
    @XmlElement
    private String telefono;
    @XmlElement
    private Date fechaNacimiento;

    public Paciente() {}

    public Paciente(int id, String nombre, String telefono, Date fechaNacimiento) {
        super(id, nombre);
        this.telefono = telefono;
        this.fechaNacimiento = fechaNacimiento;
    }
    public String getTelefono() { return telefono; }

    public Date getFechaNacimiento() { return fechaNacimiento; }

}
