package org.example.service_layer;
import org.example.domain_layer.Receta;

import java.util.List;

public class RecetaService implements IService<Receta> {

    @Override
    public void agregar(Receta entity) {

    }

    @Override
    public void borrar(int id) {

    }

    @Override
    public void actualizar(Receta entity) {

    }

    @Override
    public List<Receta> leerTodos() {
        return List.of();
    }

    @Override
    public Receta leerPorId(int id) {
        return null;
    }

    @Override
    public void addObserver(IServiceObserver<Receta> listener) {

    }
}
