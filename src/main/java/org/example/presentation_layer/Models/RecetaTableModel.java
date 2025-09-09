package org.example.presentation_layer.Models;

import org.example.domain_layer.Receta;

import javax.swing.table.AbstractTableModel;
import java.text.SimpleDateFormat;
import java.util.List;

public class RecetaTableModel extends AbstractTableModel {

    private final List<Receta> recetas;
    private final String[] columnas = {"ID", "Fecha Confección", "Fecha Retiro", "Estado"};

    public RecetaTableModel(List<Receta> recetas) {
        this.recetas = recetas;
    }

    @Override
    public int getRowCount() {
        return recetas.size();
    }

    @Override
    public int getColumnCount() {
        return columnas.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Receta r = recetas.get(rowIndex);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return switch (columnIndex) {
            case 0 -> r.getId();
            case 1 -> sdf.format(r.getFechaConfeccion());
            case 2 -> sdf.format(r.getFechaRetiro());
            //lo reviso en un momento
           // case 3 -> r.getEstado().name();
            default -> null;
        };
    }

    @Override
    public String getColumnName(int column) {
        return columnas[column];
    }

    public Receta getRecetaAt(int row) {
        return recetas.get(row);
    }
}