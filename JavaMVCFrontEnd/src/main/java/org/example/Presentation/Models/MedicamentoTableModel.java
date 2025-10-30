package org.example.Presentation.Models;

import org.example.Domain.Dtos.Medicamento.MedicamentoResponseDto;
import org.example.Utilities.ChangeType;
import org.example.Presentation.IObserver;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class MedicamentoTableModel extends AbstractTableModel implements IObserver {

    private final List<MedicamentoResponseDto> medicamentos = new ArrayList<>();
    private final String[] columnNames = {"Código", "Nombre", "Presentación"};

    @Override
    public int getRowCount() {
        return medicamentos.size();
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
        MedicamentoResponseDto m = medicamentos.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> m.getId();
            case 1 -> m.getNombre();
            case 2 -> m.getPresentacion();
            default -> null;
        };
    }

    @Override
    public void update(ChangeType changeType, Object data) {
        if (data == null) return;
        switch (changeType) {
            case CREATED -> {
                MedicamentoResponseDto nuevo = (MedicamentoResponseDto) data;
                medicamentos.add(nuevo);
                fireTableRowsInserted(medicamentos.size() - 1, medicamentos.size() - 1);
            }
            case UPDATED -> {
                MedicamentoResponseDto actualizado = (MedicamentoResponseDto) data;
                for (int i = 0; i < medicamentos.size(); i++) {
                    if (medicamentos.get(i).getId() == actualizado.getId()) {
                        medicamentos.set(i, actualizado);
                        fireTableRowsUpdated(i, i);
                        break;
                    }
                }
            }
            case DELETED -> {
                Integer deletedId = (Integer) data;
                for (int i = 0; i < medicamentos.size(); i++) {
                    if (medicamentos.get(i).getId() == deletedId) {
                        medicamentos.remove(i);
                        fireTableRowsDeleted(i, i);
                        break;
                    }
                }
            }
        }
    }

    public List<MedicamentoResponseDto> getMedicamentos() {
        return new ArrayList<>(medicamentos);
    }

    public void setMedicamentos(List<MedicamentoResponseDto> nuevos) {
        medicamentos.clear();
        if (nuevos != null) medicamentos.addAll(nuevos);
        fireTableDataChanged();
    }

    public MedicamentoResponseDto getMedicamentoAt(int row) {
        return medicamentos.get(row);
    }
}
