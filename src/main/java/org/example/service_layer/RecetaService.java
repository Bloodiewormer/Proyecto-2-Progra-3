package org.example.service_layer;

import org.example.data_access_layer.IFileStore;
import org.example.domain_layer.Receta;
import org.example.utilities.ChangeType;

import java.util.ArrayList;
import java.util.List;

public class RecetaService implements IService<Receta> {

    private final IFileStore<Receta> fileStore;
    private final List<IServiceObserver<Receta>> observers = new ArrayList<>();

    public RecetaService(IFileStore<Receta> fileStore) {
        this.fileStore = fileStore;
    }

    @Override
    public void agregar(Receta entity) {
        List<Receta> recetas = fileStore.readAll();
        recetas.add(entity);
        fileStore.writeAll(recetas);
        notifyObservers(ChangeType.UPDATED,entity);
    }

    @Override
    public void borrar(int id) {
        List<Receta> recetas = fileStore.readAll();
        recetas.removeIf(r -> r.getId() == id);
        fileStore.writeAll(recetas);
        notifyObservers(ChangeType.DELETED,null);
    }

    @Override
    public void actualizar(Receta entity) {
        List<Receta> recetas = fileStore.readAll();
        for (int i = 0; i < recetas.size(); i++) {
            if (recetas.get(i).getId() == entity.getId()) {
                recetas.set(i, entity);
                break;
            }
        }
        fileStore.writeAll(recetas);
        notifyObservers(ChangeType.UPDATED,entity);
    }

    @Override
    public List<Receta> leerTodos() {
        return fileStore.readAll();
    }

    @Override
    public Receta leerPorId(int id) {
        return fileStore.readAll()
                .stream()
                .filter(r -> r.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public void addObserver(IServiceObserver<Receta> listener) {
        observers.add(listener);
    }

    private void notifyObservers(ChangeType c, Receta entity) {
        for (IServiceObserver<Receta> obs : observers) {
            obs.onDataChanged(c,entity);
        }
    }
}