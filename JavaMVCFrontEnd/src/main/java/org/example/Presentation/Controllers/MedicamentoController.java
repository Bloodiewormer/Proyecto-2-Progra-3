package org.example.Presentation.Controllers;

import org.example.presentation_layer.Models.MedicamentoTableModel;
import org.example.presentation_layer.Views.MedicamentoForm;
import org.example.Services.MedicamentoService;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class MedicamentoController {
    private final MedicamentoForm medicamentoForm;
    private final MedicamentoService service;
    private final MedicamentoTableModel tableModel;


    public MedicamentoController(MedicamentoForm medicamentoForm, MedicamentoService service, MedicamentoTableModel tableModel) {
        this.medicamentoForm = medicamentoForm;
        this.service = service;
        this.tableModel = tableModel;
        cargarMedicamentos();
    }

    public void agregarMedicamento() {
        try {
            int codigo = Integer.parseInt(medicamentoForm.getCodigotextField().getText().trim());
            validarCodigo(codigo);
            String nombre = medicamentoForm.getNametextField().getText().trim();
            String presentacion = medicamentoForm.getPresentaciontextField().getText().trim();

            Medicamento m = new Medicamento(codigo, nombre, presentacion);
            service.agregar(m);

            cargarMedicamentos();
            limpiarCampos();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(medicamentoForm, "ID inválido", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(medicamentoForm, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }



    public void borrarMedicamento() {
        int row = medicamentoForm.getMedicamentotable().getSelectedRow();
        if (row >= 0) {
            int codigo = (int) tableModel.getValueAt(row, 0);
            service.borrar(codigo);
            cargarMedicamentos();
        }
    }

    public void buscarMedicamento() {
        String filtro = medicamentoForm.getBuscartextField().getText().toLowerCase().trim();
        List<Medicamento> medicamentos = service.leerTodos();
        List<Medicamento> filtrados = new ArrayList<>();
        for (Medicamento m : medicamentos) {
            if (m.getNombre().toLowerCase().contains(filtro) ||
                    m.getPresentacion().toLowerCase().contains(filtro) ||
                    String.valueOf(m.getCodigo()).contains(filtro)) {
                filtrados.add(m);
            }
        }
        tableModel.setMedicamentos(filtrados);
    }

    public void actualizarMedicamento() {
        try {
            int codigo = Integer.parseInt(medicamentoForm.getCodigotextField().getText().trim());
            String nombre = medicamentoForm.getNametextField().getText().trim();
            String presentacion = medicamentoForm.getPresentaciontextField().getText().trim();

            Medicamento m = new Medicamento(codigo, nombre, presentacion);
            service.actualizar(m);

            cargarMedicamentos();
            limpiarCampos();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(medicamentoForm, "ID inválido", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(medicamentoForm, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    public void limpiarCampos() {
        medicamentoForm.getCodigotextField().setText("");
        medicamentoForm.getNametextField().setText("");
        medicamentoForm.getPresentaciontextField().setText("");
    }

    private void cargarMedicamentos() {
        List<Medicamento> medicamentos = service.leerTodos();
        tableModel.setMedicamentos(medicamentos);
    }

    private void validarCodigo(int codigo) {
        for (Medicamento m : service.leerTodos()) {
            if (codigo <= 0) {
                throw new IllegalArgumentException("El código debe ser positivo");
            }
            if (m.getCodigo() == codigo) {
                throw new IllegalArgumentException("El código ya existe");
            }
        }
    }

    public void generarReporteMedicamentoSeleccionado() {
        int row = medicamentoForm.getMedicamentotable().getSelectedRow();
        if (row >= 0) {
            int id = (int) tableModel.getValueAt(row, 0);
            Medicamento u = service.leerPorId(id);
            if (u != null) {
                String reporte = "Reporte de Medicamento\n\n" +
                        "ID: " + u.getCodigo() + "\n" +
                        "Nombre: " + u.getNombre() + "\n" +
                        "Tipo: " + u.getClass().getSimpleName() + "\n";
                JOptionPane.showMessageDialog(medicamentoForm, reporte, "Reporte de Medicamento", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(medicamentoForm, "No se encontró el Medicamento seleccionado.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(medicamentoForm, "Por favor, seleccione un Medicamento de la tabla.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }




}