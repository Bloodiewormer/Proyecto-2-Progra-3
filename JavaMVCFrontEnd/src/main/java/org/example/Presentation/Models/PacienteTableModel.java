package org.example.Presentation.Models;

import org.example.Domain.Dtos.Paciente.PacienteResponseDto;
import org.example.Presentation.IObserver;
import org.example.Utilities.ChangeType;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class PacienteTableModel extends AbstractTableModel implements IObserver {

    private final List<PacienteResponseDto> pacientes = new ArrayList<>();
    private final String[] columnNames = {"ID", "Nombre", "Fecha Nacimiento", "Teléfono"};

    @Override
    public int getRowCount() {
        return pacientes.size();
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
        PacienteResponseDto p = pacientes.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> p.getId();
            case 1 -> p.getNombre();
            case 2 -> p.getFechaNacimiento();
            case 3 -> p.getTelefono();
            default -> null;
        };
    }

    @Override
    public void update(ChangeType eventType, Object data) {
        if (data == null) return;

        switch (eventType) {
            case CREATED -> {
                PacienteResponseDto nuevo = (PacienteResponseDto) data;
                pacientes.add(nuevo);
                fireTableRowsInserted(pacientes.size() - 1, pacientes.size() - 1);
            }
            case UPDATED -> {
                PacienteResponseDto actualizado = (PacienteResponseDto) data;
                for (int i = 0; i < pacientes.size(); i++) {
                    if (pacientes.get(i).getId() == actualizado.getId()) {
                        pacientes.set(i, actualizado);
                        fireTableRowsUpdated(i, i);
                        break;
                    }
                }
            }
            case DELETED -> {
                Integer deletedId = (Integer) data;
                for (int i = 0; i < pacientes.size(); i++) {
                    if (pacientes.get(i).getId() == deletedId) {
                        pacientes.remove(i);
                        fireTableRowsDeleted(i, i);
                        break;
                    }
                }
            }
        }
    }

    public List<PacienteResponseDto> getPacientes() {
        return new ArrayList<>(pacientes);
    }

    public void setPacientes(List<PacienteResponseDto> nuevos) {
        pacientes.clear();
        if (nuevos != null) pacientes.addAll(nuevos);
        fireTableDataChanged();
    }

    public PacienteResponseDto getPacienteAt(int row) {
        if (row >= 0 && row < pacientes.size()) {
            return pacientes.get(row);
        }
        return null;
    }
}