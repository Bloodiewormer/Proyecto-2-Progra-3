package org.example.service_layer;

import org.example.domain_layer.Paciente;

import java.util.List;

public class PacienteService implements IService<Paciente> {
    @Override
    public void agregar(Paciente entity) {

    }

    @Override
    public void borrar(int id) {

    }

    @Override
    public void actualizar(Paciente entity) {

    }

    @Override
    public List<Paciente> leerTodos() {
        return List.of();
    }

    @Override
    public Paciente leerPorId(int id) {
        return null;
    }

    @Override
    public void addObserver(IServiceObserver<Paciente> listener) {

    }
}
