package org.example.Services;

import org.example.data_access_layer.IFileStore;
import org.example.Utilities.ChangeType;

import java.util.ArrayList;
import java.util.List;

public class MedicamentoService implements IService<Medicamento> {

    private final IFileStore<Medicamento> fileStore;
    private final List<IServiceObserver<Medicamento>> observers = new ArrayList<>();

    public MedicamentoService(IFileStore<Medicamento> fileStore) {
        this.fileStore = fileStore;
    }

    @Override
    public void agregar(Medicamento entity) {
        List<Medicamento> medicamentos = fileStore.readAll();
        medicamentos.add(entity);
        fileStore.writeAll(medicamentos);
        notifyObservers(ChangeType.CREATED ,entity);
    }

    @Override
    public void borrar(int id) {
        List<Medicamento> medicamentos = fileStore.readAll();
        medicamentos.removeIf(m -> m.getCodigo() == id);
        fileStore.writeAll(medicamentos);
        notifyObservers(ChangeType.DELETED ,null);
    }

    @Override
    public void actualizar(Medicamento entity) {
        List<Medicamento> medicamentos = fileStore.readAll();
        for (int i = 0; i < medicamentos.size(); i++) {
            if (medicamentos.get(i).getCodigo() == entity.getCodigo()) {
                medicamentos.set(i, entity);
                break;
            }
        }
        fileStore.writeAll(medicamentos);
        notifyObservers(ChangeType.UPDATED ,entity);
    }

    @Override
    public List<Medicamento> leerTodos() {
        return fileStore.readAll();
    }

    @Override
    public Medicamento leerPorId(int id) {
        return fileStore.readAll()
                .stream()
                .filter(m -> m.getCodigo() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public void addObserver(IServiceObserver<Medicamento> listener) {
        observers.add(listener);
    }

    private void notifyObservers(ChangeType c, Medicamento entity) {
        for (IServiceObserver<Medicamento> obs : observers) {
            obs.onDataChanged(c, entity);
        }
    }


    public List<Medicamento> buscarPorNombre(String nombre) {
        List<Medicamento> result = new ArrayList<>();
        for (Medicamento m : leerTodos()) {
            if (m.getNombre().toLowerCase().contains(nombre.toLowerCase())) {
                result.add(m);
            }
        }
        return result;
    }
}