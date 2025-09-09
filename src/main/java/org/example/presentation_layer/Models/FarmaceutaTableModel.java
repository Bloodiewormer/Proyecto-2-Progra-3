package org.example.presentation_layer.Models;

import org.example.domain_layer.Farmaceuta;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class FarmaceutaTableModel extends AbstractTableModel {
    private final String[] columnNames = {"ID", "Nombre"};
    private List<Farmaceuta> farmaceuta;

    public FarmaceutaTableModel(List<Farmaceuta> farmaceuta) {
        this.farmaceuta = farmaceuta;
    }

    @Override
    public int getRowCount() {
        return farmaceuta.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Farmaceuta m = farmaceuta.get(rowIndex);
        switch (columnIndex) {
            case 0: return m.getId();
            case 1: return m.getNombre();
            default: return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    public void setFarmaceuta(List<Farmaceuta> farmaceutas) {
        this.farmaceuta = farmaceutas;
        fireTableDataChanged();
        // Notify the table that the data has changed
    }

    public Farmaceuta getFarmaceutaAt(int row) {
        return farmaceuta.get(row);
    }
}
