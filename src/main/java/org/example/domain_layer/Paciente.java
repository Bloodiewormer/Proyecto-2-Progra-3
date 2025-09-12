package org.example.domain_layer;

import java.util.Date;

import jakarta.xml.bind.annotation.*;

@XmlAccessorType (XmlAccessType.FIELD)
@XmlRootElement
@XmlSeeAlso({Usuario.class})
public class Paciente extends Persona {

    private String telefono;
    private Date fechanacimiento;

    public Paciente() {}

    public Paciente(int id, String nombre, String telefono, Date fechanacimiento) {
        super(id, nombre);
        this.telefono = telefono;
        this.fechanacimiento = fechanacimiento;
    }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public Date getFechanacimiento() { return fechanacimiento; }
    public void setFechanacimiento(Date fechanacimiento) { this.fechanacimiento = fechanacimiento; }

}
