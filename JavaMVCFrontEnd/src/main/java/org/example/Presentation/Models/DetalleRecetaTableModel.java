package org.example.Presentation.Models;


import org.example.Domain.Dtos.DetalleReceta.DetalleRecetaResponseDto;
import org.example.Presentation.IObserver;
import org.example.Utilities.ChangeType;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class DetalleRecetaTableModel extends AbstractTableModel implements IObserver {
    private final String[] columnNames = {"Medicamento", "Cantidad", "Indicaciones", "Duración (días)"};
    private final List<DetalleRecetaResponseDto> detalles = new ArrayList<>();

    @Override
    public int getRowCount() {
        return detalles.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        DetalleRecetaResponseDto detalle = detalles.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> detalle.getIdMedicamento(); // O el nombre si lo tienes
            case 1 -> detalle.getCantidad();
            case 2 -> detalle.getIndicaciones();
            case 3 -> detalle.getDias();
            default -> null;
        };
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public void update(ChangeType changeType, Object data) {
        if (data == null) return;

        switch (changeType) {
            case CREATED -> {
                DetalleRecetaResponseDto nuevo = (DetalleRecetaResponseDto) data;
                detalles.add(nuevo);
                fireTableRowsInserted(detalles.size() - 1, detalles.size() - 1);
            }
            case UPDATED -> {
                DetalleRecetaResponseDto actualizado = (DetalleRecetaResponseDto) data;
                for (int i = 0; i < detalles.size(); i++) {
                    if (detalles.get(i).getId() == actualizado.getId()) {
                        detalles.set(i, actualizado);
                        fireTableRowsUpdated(i, i);
                        break;
                    }
                }
            }
            case DELETED -> {
                Integer deletedId = (Integer) data;
                for (int i = 0; i < detalles.size(); i++) {
                    if (detalles.get(i).getId() == deletedId) {
                        detalles.remove(i);
                        fireTableRowsDeleted(i, i);
                        break;
                    }
                }
            }
        }
    }

    public List<DetalleRecetaResponseDto> getDetalles() {
        return new ArrayList<>(detalles);
    }

    public void setDetalles(List<DetalleRecetaResponseDto> nuevos) {
        detalles.clear();
        if (nuevos != null) detalles.addAll(nuevos);
        fireTableDataChanged();
    }

    public void clear() {
        detalles.clear();
        fireTableDataChanged();
    }

    public DetalleRecetaResponseDto getDetalleAt(int row) {
        return detalles.get(row);
    }
}