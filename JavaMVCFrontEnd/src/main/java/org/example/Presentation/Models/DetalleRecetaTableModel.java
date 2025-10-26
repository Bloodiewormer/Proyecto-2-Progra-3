package org.example.presentation_layer.Models;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class DetalleRecetaTableModel extends AbstractTableModel {
    private final String[] columnNames = {"Medicamento", "Cantidad", "Indicaciones", "Duracion (dias)"};
    private final List<DetalleReceta> detalleRecetaList;

    public DetalleRecetaTableModel(List<DetalleReceta> detalleRecetaList) {
        this.detalleRecetaList = detalleRecetaList;
    }

    @Override
    public int getRowCount() {
        return detalleRecetaList.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        DetalleReceta detalle = detalleRecetaList.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> detalle.getMedicamento().getNombre();
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



}
