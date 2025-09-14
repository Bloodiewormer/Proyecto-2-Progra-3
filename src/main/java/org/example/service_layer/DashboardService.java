package org.example.service_layer;

import org.example.domain_layer.Medicamento;
import org.example.domain_layer.Receta;
import org.example.utilities.ChangeType;

import java.util.ArrayList;
import java.util.List;

public class DashboardService {

    private final List<IServiceObserver<Object>> observers = new ArrayList<>();

    public DashboardService(RecetaService recetaService, MedicamentoService medicamentoService) {
        recetaService.addObserver(new RecetaObserver());
        medicamentoService.addObserver(new MedicamentoObserver());
    }

    // Inner observer for Receta
    private class RecetaObserver implements IServiceObserver<Receta> {
        @Override
        public void onDataChanged(ChangeType type, Receta entity) {
            notifyObservers(type, entity);
        }
        @Override
        public void onDataChanged(List<Receta> entities) {
            for (Receta entity : entities) {
                notifyObservers(ChangeType.UPDATED, entity);
            }
        }
    }

    // Inner observer for Medicamento
    private class MedicamentoObserver implements IServiceObserver<Medicamento> {
        @Override
        public void onDataChanged(ChangeType type, Medicamento entity) {
            notifyObservers(type, entity);
        }
        @Override
        public void onDataChanged(List<Medicamento> entities) {
            for (Medicamento entity : entities) {
                notifyObservers(ChangeType.UPDATED, entity);
            }
        }
    }

    public void addObserver(IServiceObserver<Object> observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    private void notifyObservers(ChangeType type, Object entity) {
        for (IServiceObserver<Object> obs : observers) {
            obs.onDataChanged(type, entity);
        }
    }
}
