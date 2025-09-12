package org.example.presentation_layer.Views;

import org.example.domain_layer.Paciente;
import org.example.domain_layer.Receta;
import org.example.presentation_layer.Controllers.HistorialRecetaController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class HistorialRecetaForm extends JFrame {

    private final HistorialRecetaController controller;

    private JComboBox<String> comboBusqueda;
    private JTextField txtBusqueda;
    private JButton btnBuscar;
    private JTable tablaRecetas;
    private DefaultTableModel modeloTabla;

    public HistorialRecetaForm(HistorialRecetaController controller) {
        this.controller = controller;
        initUI();
    }

    private void initUI() {
        setTitle("Historial de Recetas por Paciente");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel búsqueda
        JPanel panelBusqueda = new JPanel(new FlowLayout());
        comboBusqueda = new JComboBox<>(new String[]{"Buscar por ID", "Buscar por Nombre"});
        txtBusqueda = new JTextField(15);
        btnBuscar = new JButton("Buscar");

        panelBusqueda.add(comboBusqueda);
        panelBusqueda.add(txtBusqueda);
        panelBusqueda.add(btnBuscar);

        // Tabla
        modeloTabla = new DefaultTableModel(new String[]{"ID Receta", "Fecha", "Estado", "Medicamentos"}, 0);
        tablaRecetas = new JTable(modeloTabla);

        JScrollPane scrollPane = new JScrollPane(tablaRecetas);

        // Layout
        setLayout(new BorderLayout());
        add(panelBusqueda, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Acción buscar
        btnBuscar.addActionListener(e -> buscarPaciente());
    }

    private void buscarPaciente() {
        String criterio = (String) comboBusqueda.getSelectedItem();
        String valor = txtBusqueda.getText().trim();

        if (valor.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese un valor de búsqueda", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Paciente paciente = null;

        try {
            if ("Buscar por ID".equals(criterio)) {
                int id = Integer.parseInt(valor);
                paciente = controller.buscarPacientePorId(id);
            } else {
                List<Paciente> pacientes = controller.buscarPacientePorNombre(valor);
                if (pacientes.size() == 1) {
                    paciente = pacientes.get(0);
                } else if (pacientes.size() > 1) {
                    JOptionPane.showMessageDialog(this, "Se encontraron varios pacientes con ese nombre", "Atención", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El ID debe ser un número válido", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (paciente == null) {
            JOptionPane.showMessageDialog(this, "Paciente no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        mostrarHistorial(paciente);
    }

    private void mostrarHistorial(Paciente paciente) {
        modeloTabla.setRowCount(0);
        List<Receta> recetas = controller.obtenerHistorialRecetas(paciente);

        for (Receta r : recetas) {
            modeloTabla.addRow(new Object[]{
                    r.getId(),
                    r.getFecha(),
                    r.getEstado(),
                    r.getMedicamentos().toString()
            });
        }
    }
}

