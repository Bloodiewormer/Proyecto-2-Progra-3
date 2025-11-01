package org.example.Presentation.Models;

import org.example.Domain.Dtos.Farmaceuta.FarmaceutaResponseDto;
import org.example.Utilities.ChangeType;
import org.example.Presentation.IObserver;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class FarmaceutaTableModel extends AbstractTableModel implements IObserver {

    private final List<FarmaceutaResponseDto> farmaceutas = new ArrayList<>();
    private final String[] columnNames = {"ID", "Nombre"};

    @Override
    public int getRowCount() {
        return farmaceutas.size();
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
        FarmaceutaResponseDto f = farmaceutas.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> f.getId();
            case 1 -> f.getNombre();
            default -> null;
        };
    }

    @Override
    public void update(ChangeType changeType, Object data) {
        if (data == null) return;
        switch (changeType) {
            case CREATED -> {
                FarmaceutaResponseDto nuevo = (FarmaceutaResponseDto) data;
                farmaceutas.add(nuevo);
                fireTableRowsInserted(farmaceutas.size() - 1, farmaceutas.size() - 1);
            }
            case UPDATED -> {
                FarmaceutaResponseDto actualizado = (FarmaceutaResponseDto) data;
                for (int i = 0; i < farmaceutas.size(); i++) {
                    if (farmaceutas.get(i).getId() == actualizado.getId()) {
                        farmaceutas.set(i, actualizado);
                        fireTableRowsUpdated(i, i);
                        break;
                    }
                }
            }
            case DELETED -> {
                Integer deletedId = (Integer) data;
                for (int i = 0; i < farmaceutas.size(); i++) {
                    if (farmaceutas.get(i).getId() == deletedId) {
                        farmaceutas.remove(i);
                        fireTableRowsDeleted(i, i);
                        break;
                    }
                }
            }
        }
    }

    public List<FarmaceutaResponseDto> getFarmaceutas() {
        return new ArrayList<>(farmaceutas);
    }

    public void setFarmaceutas(List<FarmaceutaResponseDto> nuevos) {
        farmaceutas.clear();
        if (nuevos != null) farmaceutas.addAll(nuevos);
        fireTableDataChanged();
    }

    public FarmaceutaResponseDto getFarmaceutaAt(int row) {
        return farmaceutas.get(row);
    }
}