package org.example.presentation_layer.Views;

import org.example.domain_layer.Medicamento;
import org.example.presentation_layer.Controllers.MedicamentoController;
import org.example.presentation_layer.Models.MedicamentoTableModel;

import javax.swing.*;
import java.util.List;

public class MedicamentoForm {
    private JPanel mainPanel;
    private JTable medicamentoTable;
    private JButton agregarButton;
    private JButton borrarButton;
    private JButton actualizarButton;

    private final MedicamentoController controller;

    public MedicamentoForm() {
        // Aquí inyectas el controlador real
        this.controller = new MedicamentoController();

        // Eventos
        agregarButton.addActionListener(e -> agregarMedicamento());
        borrarButton.addActionListener(e -> borrarMedicamento());
        actualizarButton.addActionListener(e -> actualizarTabla());

        actualizarTabla();
    }

    private void agregarMedicamento() {
        JTextField codigoField = new JTextField();
        JTextField nombreField = new JTextField();
        JTextField presentacionField = new JTextField();

        Object[] message = {
                "Código:", codigoField,
                "Nombre:", nombreField,
                "Presentación:", presentacionField
        };

        int option = JOptionPane.showConfirmDialog(mainPanel, message, "Agregar Medicamento", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                int codigo = Integer.parseInt(codigoField.getText().trim());
                String nombre = nombreField.getText().trim();
                String presentacion = presentacionField.getText().trim();

                controller.agregar(new Medicamento(codigo, nombre, presentacion));
                actualizarTabla();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(mainPanel, "El código debe ser un número válido", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void borrarMedicamento() {
        int fila = medicamentoTable.getSelectedRow();
        if (fila >= 0) {
            int codigo = (int) medicamentoTable.getValueAt(fila, 0);
            controller.borrar(codigo);
            actualizarTabla();
        } else {
            JOptionPane.showMessageDialog(mainPanel, "Seleccione un medicamento para borrar", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void actualizarTabla() {
        List<Medicamento> medicamentos = controller.leerTodos();
        medicamentoTable.setModel(new MedicamentoTableModel(medicamentos));
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}