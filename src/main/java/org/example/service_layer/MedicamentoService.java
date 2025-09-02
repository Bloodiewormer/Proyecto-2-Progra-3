package org.example.service_layer;

import org.example.domain_layer.Medicamento;

import java.util.ArrayList;
import java.util.List;

public class MedicamentoService implements IService<Medicamento> {
    @Override
    public void agregar(Medicamento entity) {
       // List <Medicamento> listaMedicamentos

    }

    @Override
    public void borrar(int id) {

    }

    @Override
    public void actualizar(Medicamento entity) {

    }

    @Override
    public List<Medicamento> leerTodos() {
        return List.of();
    }

    @Override
    public Medicamento leerPorId(int id) {
        return null;
    }

    @Override
    public void addObserver(IServiceObserver<Medicamento> listener) {

    }
}
