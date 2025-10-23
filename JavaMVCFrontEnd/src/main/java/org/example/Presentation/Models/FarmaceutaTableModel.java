package org.example.presentation_layer.Models;

import org.example.Domain.Dtos.users.Farmaceuta;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class FarmaceutaTableModel extends AbstractTableModel {
    private final String[] columnNames = {"ID", "Nombre"};
    private List<Farmaceuta> farmaceutas;

    public FarmaceutaTableModel(List<Farmaceuta> farmaceutas) {
        this.farmaceutas = farmaceutas;
    }


    @Override
    public int getRowCount() {
        return farmaceutas.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Farmaceuta m = farmaceutas.get(rowIndex);
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
        this.farmaceutas = farmaceutas;
        fireTableDataChanged();
        // Notify the table that the data has changed
    }

    public Farmaceuta getFarmaceutaAt(int row) {
        return farmaceutas.get(row);
    }



    public void setRows(List<Farmaceuta> data) {
        farmaceutas.clear();
        if (data != null) farmaceutas.addAll(data);
        fireTableDataChanged();
    }

    private int indexOf(Farmaceuta m) {
        for (int i = 0; i < farmaceutas.size(); i++) {
            if (farmaceutas.get(i).getId() == m.getId()) {
                return i;
            }
        }
        return -1;
    }
}
