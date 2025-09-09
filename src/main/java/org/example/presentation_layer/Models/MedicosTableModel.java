package org.example.presentation_layer.Models;

import org.example.domain_layer.Medico;
import javax.swing.table.AbstractTableModel;
import java.util.List;

public class MedicosTableModel extends AbstractTableModel {
    private final String[] columnNames = {"ID", "Nombre", "Especialidad"};
    private List<Medico> medicos;

    public MedicosTableModel(List<Medico> medicos) {
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
        // Notify the table that the data has changed
    }

    public Medico getMedicoAt(int row) {
        return medicos.get(row);
    }
}
