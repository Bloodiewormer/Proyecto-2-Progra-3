package org.example.Presentation.Models;

import org.example.Domain.Dtos.Medico.MedicoResponseDto;
import org.example.Presentation.IObserver;
import org.example.Utilities.EventType;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class MedicoTableModel extends AbstractTableModel implements IObserver {

    private final List<MedicoResponseDto> medicos = new ArrayList<>();
    private final String[] columnNames = {"ID", "Nombre", "Especialidad"};

    @Override
    public int getRowCount() {
        return medicos.size();
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
        MedicoResponseDto m = medicos.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> m.getId();
            case 1 -> m.getNombre();
            case 2 -> m.getEspecialidad();
            default -> null;
        };
    }

    @Override
    public void update(EventType eventType, Object data) {
        if (data == null) return;

        switch (eventType) {
            case CREATED -> {
                MedicoResponseDto nuevo = (MedicoResponseDto) data;
                medicos.add(nuevo);
                fireTableRowsInserted(medicos.size() - 1, medicos.size() - 1);
            }
            case UPDATED -> {
                MedicoResponseDto actualizado = (MedicoResponseDto) data;
                for (int i = 0; i < medicos.size(); i++) {
                    if (medicos.get(i).getId() == actualizado.getId()) {
                        medicos.set(i, actualizado);
                        fireTableRowsUpdated(i, i);
                        break;
                    }
                }
            }
            case DELETED -> {
                Integer deletedId = (Integer) data;
                for (int i = 0; i < medicos.size(); i++) {
                    if (medicos.get(i).getId() == deletedId) {
                        medicos.remove(i);
                        fireTableRowsDeleted(i, i);
                        break;
                    }
                }
            }
        }
    }

    public List<MedicoResponseDto> getMedicos() {
        return new ArrayList<>(medicos);
    }

    public void setMedicos(List<MedicoResponseDto> nuevos) {
        medicos.clear();
        if (nuevos != null) medicos.addAll(nuevos);
        fireTableDataChanged();
    }

    public MedicoResponseDto getMedicoAt(int row) {
        return medicos.get(row);
    }
}