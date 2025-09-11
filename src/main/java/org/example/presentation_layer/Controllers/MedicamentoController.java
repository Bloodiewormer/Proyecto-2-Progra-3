package org.example.presentation_layer.Controllers;

import org.example.domain_layer.Medicamento;
import org.example.presentation_layer.Models.MedicamentoTableModel;
import org.example.presentation_layer.Models.MedicosTableModel;
import org.example.presentation_layer.Views.MedicamentoForm;
import org.example.service_layer.MedicamentoService;
import org.example.service_layer.IServiceObserver;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class MedicamentoController {
    private final MedicamentoForm medicamentoForm;
    private final MedicamentoService service;
    private final MedicamentoTableModel tableModel;


    public MedicamentoController(MedicamentoForm medicamentoForm, MedicamentoService service) {
        this.medicamentoForm = medicamentoForm;
        this.service = service;
        this.tableModel = new MedicamentoTableModel(new ArrayList<>());
        this.medicamentoForm.getMedicamentostable().setModel(tableModel);
        cargarMedicamentos();
    }

    public void agregarMedicamento() {
        try {
            int codigo = Integer.parseInt(medicamentoForm.getCodigotextField().getText().trim());
            String nombre = medicamentoForm.getNametextField().getText().trim();
            String presentacion = medicamentoForm.getPresentaciontextField().getText().trim();

            Medicamento m = new Medicamento(codigo, nombre, presentacion);
            service.agregar(m);

            cargarMedicamentos();
            limpiarCampos();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(medicamentoForm, "ID inválido" , "Error", JOptionPane.ERROR_MESSAGE);

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(medicamentoForm, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    public void borrarMedicamento() {
        int row = medicamentoForm.getMedicamentostable().getSelectedRow();
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
            // Manejar error de formato
        } catch (IllegalArgumentException ex) {
            // Manejar error de argumento ilegal
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






}