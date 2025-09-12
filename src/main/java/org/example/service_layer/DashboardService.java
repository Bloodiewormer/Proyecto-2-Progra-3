package org.example.service_layer;

import org.example.domain_layer.Medicamento;
import org.example.domain_layer.Receta;
import org.example.utilities.ChangeType;

import java.util.ArrayList;
import java.util.List;

public class DashboardService implements IServiceObserver<Object> {

    private final List<IServiceObserver<Object>> observers = new ArrayList<>();

    public DashboardService(RecetaService recetaService, MedicamentoService medicamentoService) {
        // Se suscribe a cambios en recetas y medicamentos
       // recetaService.addObserver(this);
       // medicamentoService.addObserver(this);
    }

    @Override
    public void onDataChanged(ChangeType type, Object entity) {
        // Cuando cambia algo, notificamos a los listeners (ej: DashboardView)
        notifyObservers(type, entity);
    }

    @Override
    public void onDataChanged(List<Object> entities) {
        // También soportamos notificación en lote
        for (Object entity : entities) {
            notifyObservers(ChangeType.UPDATED, entity);
        }
    }

    public void addObserver(IServiceObserver<Object> observer) {
        if (observer != null) {
            observers.add(observer);
        }
    }

    private void notifyObservers(ChangeType type, Object entity) {
        for (IServiceObserver<Object> obs : observers) {
            obs.onDataChanged(type, entity);
        }
    }
}
