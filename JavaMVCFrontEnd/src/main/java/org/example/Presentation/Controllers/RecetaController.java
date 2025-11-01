package org.example.Presentation.Controllers;

import org.example.Domain.Dtos.Receta.*;
import org.example.Presentation.Observable;
import org.example.Services.RecetaService;
import org.example.Utilities.ChangeType;


import javax.swing.*;
import java.util.List;

public class RecetaController extends Observable {
    private final RecetaService recetaService;

    public RecetaController(RecetaService recetaService) {
        this.recetaService = recetaService;
    }

    public void agregarRecetaAsync(AddRecetaRequestDto dto, RecetaCallback callback) {
        SwingWorker<RecetaResponseDto, Void> worker = new SwingWorker<>() {
            @Override
            protected RecetaResponseDto doInBackground() throws Exception {
                return recetaService.addRecetaAsync(dto).get();
            }

            @Override
            protected void done() {
                try {
                    RecetaResponseDto receta = get();
                    if (receta != null) {
                        notifyObservers(ChangeType.CREATED, receta);
                        callback.onSuccess(receta);
                    } else {
                        callback.onError("No se pudo agregar la receta");
                    }
                } catch (Exception ex) {
                    callback.onError("Error: " + ex.getMessage());
                }
            }
        };
        worker.execute();
    }

    public void actualizarRecetaAsync(UpdateRecetaRequestDto dto, RecetaCallback callback) {
        SwingWorker<RecetaResponseDto, Void> worker = new SwingWorker<>() {
            @Override
            protected RecetaResponseDto doInBackground() throws Exception {
                return recetaService.updateRecetaAsync(dto).get();
            }

            @Override
            protected void done() {
                try {
                    RecetaResponseDto receta = get();
                    if (receta != null) {
                        notifyObservers(ChangeType.UPDATED, receta);
                        callback.onSuccess(receta);
                    } else {
                        callback.onError("No se pudo actualizar la receta");
                    }
                } catch (Exception ex) {
                    callback.onError("Error: " + ex.getMessage());
                }
            }
        };
        worker.execute();
    }

    public void borrarRecetaAsync(int id, DeleteCallback callback) {
        SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                return recetaService.deleteRecetaAsync(new DeleteRecetaRequestDto(id)).get();
            }

            @Override
            protected void done() {
                try {
                    Boolean success = get();
                    if (Boolean.TRUE.equals(success)) {
                        notifyObservers(ChangeType.DELETED, id);
                        callback.onSuccess();
                    } else {
                        callback.onError("No se pudo eliminar la receta");
                    }
                } catch (Exception ex) {
                    callback.onError("Error: " + ex.getMessage());
                }
            }
        };
        worker.execute();
    }

    public void listarRecetasAsync(ListCallback callback) {
        SwingWorker<List<RecetaResponseDto>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<RecetaResponseDto> doInBackground() throws Exception {
                return recetaService.listRecetasAsync().get();
            }

            @Override
            protected void done() {
                try {
                    List<RecetaResponseDto> recetas = get();
                    callback.onSuccess(recetas);
                } catch (Exception ex) {
                    callback.onError("Error: " + ex.getMessage());
                }
            }
        };
        worker.execute();
    }

    // Callbacks
    public interface RecetaCallback {
        void onSuccess(RecetaResponseDto receta);
        void onError(String message);
    }

    public interface DeleteCallback {
        void onSuccess();
        void onError(String message);
    }

    public interface ListCallback {
        void onSuccess(List<RecetaResponseDto> recetas);
        void onError(String message);
    }
}