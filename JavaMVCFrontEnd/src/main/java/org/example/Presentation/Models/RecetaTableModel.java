package org.example.Presentation.Models;

import org.example.Domain.Dtos.Receta.RecetaResponseDto;
import org.example.Presentation.IObserver;
import org.example.Utilities.ChangeType;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class RecetaTableModel extends AbstractTableModel implements IObserver {

    private final List<RecetaResponseDto> recetas = new ArrayList<>();
    private final String[] columnNames = {"ID", "Paciente", "Médico", "Fecha Confección", "Fecha Retiro", "Estado"};

    @Override
    public int getRowCount() {
        return recetas.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        RecetaResponseDto receta = recetas.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> receta.getId();
            case 1 -> receta.getNombrePaciente();  // ✅ Cambiar a nombre
            case 2 -> receta.getNombreMedico();    // ✅ Cambiar a nombre
            case 3 -> receta.getFechaConfeccion();
            case 4 -> receta.getFechaRetiro();
            case 5 -> receta.getEstado();
            default -> null;
        };
    }

    @Override
    public void update(ChangeType changeType, Object data) {
        if (data == null) return;
        switch (changeType) {
            case CREATED -> {
                RecetaResponseDto nueva = (RecetaResponseDto) data;
                recetas.add(nueva);
                fireTableRowsInserted(recetas.size() - 1, recetas.size() - 1);
            }
            case UPDATED -> {
                RecetaResponseDto actualizada = (RecetaResponseDto) data;
                for (int i = 0; i < recetas.size(); i++) {
                    if (recetas.get(i).getId() == actualizada.getId()) {
                        recetas.set(i, actualizada);
                        fireTableRowsUpdated(i, i);
                        break;
                    }
                }
            }
            case DELETED -> {
                Integer deletedId = (Integer) data;
                for (int i = 0; i < recetas.size(); i++) {
                    if (recetas.get(i).getId() == deletedId) {
                        recetas.remove(i);
                        fireTableRowsDeleted(i, i);
                        break;
                    }
                }
            }
        }
    }

    public List<RecetaResponseDto> getRecetas() {
        return new ArrayList<>(recetas);
    }

    public void setRecetas(List<RecetaResponseDto> nuevas) {
        recetas.clear();
        if (nuevas != null) recetas.addAll(nuevas);
        fireTableDataChanged();
    }

    public RecetaResponseDto getRecetaAt(int row) {
        if (row >= 0 && row < recetas.size()) {
            return recetas.get(row);
        }
        return null;
    }

    public RecetaResponseDto getRecetaById(int id) {
        return recetas.stream()
                .filter(r -> r.getId() == id)
                .findFirst()
                .orElse(null);
    }
}