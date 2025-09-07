package org.example.presentation_layer.Views;

import org.example.domain_layer.Receta;
import org.example.presentation_layer.Controllers.RecetaController;
import org.example.presentation_layer.Models.RecetaTableModel;

import javax.swing.*;
import java.util.List;

public class RecetaForm {
    private JPanel mainPanel;
    private JTable recetaTable;
    private JButton agregarButton;
    private JButton borrarButton;
    private JButton actualizarButton;

    private final RecetaController controller;

    public RecetaForm() {
        this.controller = new RecetaController();

        agregarButton.addActionListener(e -> agregarReceta());
        borrarButton.addActionListener(e -> borrarReceta());
        actualizarButton.addActionListener(e -> actualizarTabla());

        actualizarTabla();
    }

    private void agregarReceta() {
        JTextField idPacienteField = new JTextField();
        JTextField idMedicoField = new JTextField();
        JTextField estadoField = new JTextField();

        Object[] message = {
                "ID Paciente:", idPacienteField,
                "ID Médico:", idMedicoField,
                "Estado:", estadoField
        };

        int option = JOptionPane.showConfirmDialog(mainPanel, message, "Agregar Receta", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                int idPaciente = Integer.parseInt(idPacienteField.getText().trim());
                int idMedico = Integer.parseInt(idMedicoField.getText().trim());
                String estado = estadoField.getText().trim();

                Receta receta = new Receta();
                receta.setIdPaciente(idPaciente);
                receta.setIdMedico(idMedico);
                receta.setEstado(estado);

                controller.agregar(receta);
                actualizarTabla();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(mainPanel, "Los IDs deben ser números válidos", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void borrarReceta() {
        int fila = recetaTable.getSelectedRow();
        if (fila >= 0) {
            int id = (int) recetaTable.getValueAt(fila, 0);
            controller.borrar(id);
            actualizarTabla();
        } else {
            JOptionPane.showMessageDialog(mainPanel, "Seleccione una receta para borrar", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void actualizarTabla() {
        List<Receta> recetas = controller.leerTodos();
        recetaTable.setModel(new RecetaTableModel(recetas));
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}