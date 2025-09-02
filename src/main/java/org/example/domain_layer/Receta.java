package org.example.domain_layer;

import org.example.utilities.EstadoReceta;

import java.util.Date;
import java.util.List;

public class Receta {
    private String ID;
    private Date fechaConfeccion;
    private Date fechaRetiro;
    private EstadoReceta estado;
    private List<DetalleReceta> detalleReceta;


    public Receta(String ID, Date fechaConfeccion, Date fechaRetiro, EstadoReceta estado, List<DetalleReceta> detalleReceta) {
        this.ID = ID;
        this.fechaConfeccion = fechaConfeccion;
        this.fechaRetiro = fechaRetiro;
        this.estado = estado;
        this.detalleReceta = detalleReceta;
    }


}