package org.example.presentation_layer.Models;

import org.example.domain_layer.Medicamento;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class MedicamentoTableModel extends AbstractTableModel {

    private final List<Medicamento> medicamentos;
    private final String[] columnas = {"Código", "Nombre", "Presentación"};

    public MedicamentoTableModel(List<Medicamento> medicamentos) {
        this.medicamentos = medicamentos;
    }

    @Override
    public int getRowCount() {
        return medicamentos.size();
    }

    @Override
    public int getColumnCount() {
        return columnas.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Medicamento m = medicamentos.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> m.getCodigo();
            case 1 -> m.getNombre();
            case 2 -> m.getPresentacion();
            default -> null;
        };
    }

    @Override
    public String getColumnName(int column) {
        return columnas[column];
    }

    public Medicamento getMedicamentoAt(int row) {
        return medicamentos.get(row);
    }
}