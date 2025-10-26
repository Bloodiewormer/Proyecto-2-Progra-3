package org.example.presentation_layer.Models;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class MedicoTableModel extends AbstractTableModel {
    private final String[] columnNames = {"ID", "Nombre", "Especialidad"};
    private List<Medico> medicos;

    public MedicoTableModel(List<Medico> medicos) {
        this.medicos = medicos;
    }

    @Override
    public int getRowCount() {
        return medicos.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Medico m = medicos.get(rowIndex);
        switch (columnIndex) {
            case 0: return m.getId();
            case 1: return m.getNombre();
            case 2: return m.getEspecialidad();
            default: return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    public void setMedicos(List<Medico> medicos) {
        this.medicos = medicos;
        fireTableDataChanged();
    }

    public Medico getMedicoAt(int row) {
        return medicos.get(row);
    }

    public void setRows(List<Medico> data) {
        medicos.clear();
        if (data != null) medicos.addAll(data);
        fireTableDataChanged();
    }

    private int indexOf(Medico m) {
        for (int i = 0; i < medicos.size(); i++) {
            if (medicos.get(i).getId() == m.getId()) {
                return i;
            }
        }
        return -1;
    }
}
