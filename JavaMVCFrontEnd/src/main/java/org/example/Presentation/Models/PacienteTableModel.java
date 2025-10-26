package org.example.presentation_layer.Models;

import javax.swing.table.AbstractTableModel;
import java.text.SimpleDateFormat;
import java.util.List;

public class PacienteTableModel extends AbstractTableModel {
    private final String[] columnNames = {"ID", "Nombre","Fecha de nacimiento", "Teléfono"};
    private List<Paciente> pacientes;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public PacienteTableModel(List<Paciente> pacientes) {
        this.pacientes = pacientes;
    }

    @Override public int getRowCount() {
        return pacientes.size();
    }
    @Override public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Paciente p = pacientes.get(rowIndex);
        switch (columnIndex) {
            case 0: return p.getId();
            case 1: return p.getNombre();
            case 2: return p.getFechaNacimiento() != null ? dateFormat.format(p.getFechaNacimiento()) : "";
            case 3: return p.getTelefono();
            default: return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    public void setPacientes(List<Paciente> pacientes) {
        this.pacientes = pacientes;
        fireTableDataChanged();
    }



    public Paciente getPacienteAt(int row) {
        return pacientes.get(row);
    }

    public void setRows(List<Paciente> data) {
        pacientes.clear();
        if (data != null) pacientes.addAll(data);
        fireTableDataChanged();
    }

    private int indexOf(Paciente p) {
        for (int i = 0; i < pacientes.size(); i++) {
            if (pacientes.get(i).getId() == p.getId()) {
                return i;
            }
        }
        return -1;
    }


}
