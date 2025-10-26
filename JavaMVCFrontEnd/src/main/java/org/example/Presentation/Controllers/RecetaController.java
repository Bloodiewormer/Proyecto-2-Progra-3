package org.example.presentation_layer.Controllers;

import org.example.service_layer.RecetaService;
import org.example.service_layer.IServiceObserver;

import java.util.List;

public class RecetaController {

    private final RecetaService service;

    public RecetaController(RecetaService service) {
        this.service = service;
    }

    public void agregarReceta(Receta r) {
        service.agregar(r);
    }

    public void borrarReceta(int id) {
        service.borrar(id);
    }

    public void actualizarReceta(Receta r) {
        service.actualizar(r);
    }

    public List<Receta> leerTodos() {
        return service.leerTodos();
    }

    public Receta leerPorId(int id) {
        return service.leerPorId(id);
    }

    public void addObserver(IServiceObserver<Receta> observer) {
        service.addObserver(observer);
    }

    public void borrar(int id) {
    }

    public void agregar(Receta receta) {
    }
}