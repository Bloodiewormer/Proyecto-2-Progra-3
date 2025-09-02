package org.example.domain_layer;

import java.util.Date;

import jakarta.xml.bind.annotation.*;

@XmlAccessorType (XmlAccessType.FIELD)
@XmlSeeAlso({Usuario.class})
public class Paciente extends Persona {
    private String telefono;
    private Date fechanacimiento;

    public Paciente(int id, String nombre, String telefono, Date fechanacimiento) {
        super(id, nombre);
        this.telefono = telefono;
        this.fechanacimiento = fechanacimiento;
    }



}
