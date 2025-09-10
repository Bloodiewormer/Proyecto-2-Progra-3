package org.example.presentation_layer.Models;

import org.example.domain_layer.Paciente;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class PacienteTableModel extends AbstractTableModel {
    private final String[] columnNames = {"ID", "Nombre","Fecha de nacimiento", "Teléfono"};
    private List<Paciente> pacientes;

    public PacienteTableModel(List<Paciente> pacientes) {
        this.pacientes = pacientes;
    }

    @Override
    public int getRowCount() {
        return pacientes.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Paciente p = pacientes.get(rowIndex);
        switch (columnIndex) {
            case 0: return p.getId();
            case 1: return p.getNombre();
//            case 2: return p.();
//            case 3: return p. ();
            default: return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    public void setMedicos(List<Paciente> medicos) {
        this.pacientes = medicos;
        fireTableDataChanged();
        // Notify the table that the data has changed
    }

    public Paciente getPacienteAt(int row) {
        return pacientes.get(row);
    }
}
