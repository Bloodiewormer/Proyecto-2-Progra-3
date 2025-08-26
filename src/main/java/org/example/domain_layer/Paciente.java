package org.example.domain_layer;

import java.util.Date;

public class Paciente {
    private int id;
    private String nombre;
    private String telefono;
    private Date fechanacimiento;

    public Paciente(int id, String nombre, String telefono, Date fechanacimiento) {
        this.id = id;
        this.nombre = nombre;
        this.telefono = telefono;
        this.fechanacimiento = fechanacimiento;
    }



}
