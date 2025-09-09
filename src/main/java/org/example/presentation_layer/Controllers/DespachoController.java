package org.example.presentation_layer.Controllers;

import org.example.domain_layer.Medicamento;
import org.example.domain_layer.Receta;
import org.example.service_layer.IService;
import org.example.utilities.EstadoReceta;

import java.util.List;

public class DespachoController {

    private final IService<Receta> recetaService;
    private final IService<Medicamento> medicamentoService;

    public DespachoController(IService<Receta> recetaService, IService<Medicamento> medicamentoService) {
        this.recetaService = recetaService;
        this.medicamentoService = medicamentoService;
    }

    public List<Receta> obtenerRecetas() {
        return recetaService.leerTodos();
    }

    public List<Medicamento> obtenerMedicamentos() {
        return medicamentoService.leerTodos();
    }

    public void despacharReceta(int recetaId) {
        Receta receta = recetaService.leerPorId(recetaId);
        if (receta != null) {
            receta.setEstado(EstadoReceta.ENTREGADA);
            recetaService.actualizar(receta);
        }
    }

    public void actualizarEstadoReceta(int recetaId, EstadoReceta nuevoEstado) {
        Receta receta = recetaService.leerPorId(recetaId);
        if (receta != null) {
            receta.setEstado(nuevoEstado);
            recetaService.actualizar(receta);
        }
    }

    public Receta obtenerRecetaPorId(int id) {
        return recetaService.leerPorId(id);
    }
}
