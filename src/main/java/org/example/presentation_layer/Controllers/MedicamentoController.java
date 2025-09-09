package org.example.presentation_layer.Controllers;

import org.example.domain_layer.Medicamento;
import org.example.service_layer.MedicamentoService;
import org.example.service_layer.IServiceObserver;

import java.util.List;

public class MedicamentoController {

    private final MedicamentoService service;

    public MedicamentoController(MedicamentoService service) {
        this.service = service;
    }

    public void agregarMedicamento(Medicamento m) {
        service.agregar(m);
    }

    public void borrarMedicamento(int codigo) {
        service.borrar(codigo);
    }

    public void actualizarMedicamento(Medicamento m) {
        service.actualizar(m);
    }

    public List<Medicamento> leerTodos() {
        return service.leerTodos();
    }

    public Medicamento leerPorId(int codigo) {
        return service.leerPorId(codigo);
    }

    public void addObserver(IServiceObserver<Medicamento> observer) {
        service.addObserver(observer);
    }

    public void agregar(Medicamento medicamento) {
    }

    public void borrar(int codigo) {
    }
}