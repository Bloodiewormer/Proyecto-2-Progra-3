package org.example.presentation_layer.Views;

import org.example.domain_layer.Paciente;
import org.example.domain_layer.Receta;
import org.example.presentation_layer.Components.BlueRoundedButton;
import org.example.presentation_layer.Models.RecetaTableModel;
import org.example.presentation_layer.Controllers.DespachoController;
import org.example.service_layer.MedicamentoService;
import org.example.service_layer.PacienteService;
import org.example.service_layer.RecetaService;
import org.example.utilities.EstadoReceta;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

public class DespachoForm extends JPanel {

    private JPanel MainPanel;
    private JPanel searchPanel;
    private JPanel infoPanel;
    private JPanel estadoPanel;
    private JPanel tablePanel;
    private JTextField DatoPacienteField;
    private JButton limpiarButton;
    private JTable recetasTable;
    private JScrollPane scrollPane;
    private JLabel pacienteInfoLabel;
    private JLabel telefonoLabel;
    private JLabel fechaNacimientoLabel;
    private JComboBox<EstadoReceta> estadoComboBox;
    private JButton cambiarEstadoButton;
    private JComboBox comboBox1;
    private JButton buscarButton;

    private final PacienteService pacienteService;
    private final RecetaService recetaService;
    private final MedicamentoService medicamentoService;
    private final DespachoController controller;



    private RecetaTableModel recetaTableModel;
    private Integer pacienteSeleccionadoId;
    private Integer recetaSeleccionadaId;

    public DespachoForm(PacienteService pacienteService,
                        RecetaService recetaService,
                        MedicamentoService medicamentoService) {
        this.pacienteService = pacienteService;
        this.recetaService = recetaService;
        this.medicamentoService = medicamentoService;
        this.controller = new DespachoController(recetaService, medicamentoService);

        comboBox1.setModel(new DefaultComboBoxModel<>(new String[]{"ID", "Nombre"}));

        setLayout(new BorderLayout());
        add(MainPanel, BorderLayout.CENTER);

        initTable();
        initEstadoCombo();
        wireEvents();
    }

    public JPanel getMainPanel() {
        return MainPanel;
    }

    private void initTable() {
        recetaTableModel = new RecetaTableModel(controller.obtenerRecetas());
        recetasTable.setModel(recetaTableModel);
        recetasTable.getSelectionModel().addListSelectionListener(this::onTableSelection);
    }

    private void initEstadoCombo() {
        estadoComboBox.removeAllItems();
        for (EstadoReceta e : EstadoReceta.values()) {
            estadoComboBox.addItem(e);
        }
    }

    private void wireEvents() {
        buscarButton.addActionListener(e -> buscarPaciente());
        limpiarButton.addActionListener(e -> limpiar());
        cambiarEstadoButton.addActionListener(e -> cambiarEstado());
    }

    private void buscarPaciente() {
        String tipo = (String) comboBox1.getSelectedItem();
        if ("ID".equals(tipo)) {
            buscarPorId();
        } else if ("Nombre".equals(tipo)) {
            buscarPorNombre();
        }
    }

    private void buscarPorId() {
        recetaSeleccionadaId = null;
        try {
            int id = Integer.parseInt(DatoPacienteField.getText().trim());
            Paciente p = pacienteService.leerPorId(id);
            if (p == null) {
                mostrarMsg("Paciente no encontrado");
                return;
            }
            pacienteSeleccionadoId = p.getId();
            mostrarPaciente(p);
            cargarRecetasPaciente();
        } catch (NumberFormatException ex) {
            mostrarMsg("ID inválido");
        }
    }

    private void buscarPorNombre() {
        recetaSeleccionadaId = null;
        String filtro = DatoPacienteField.getText().trim().toLowerCase();
        if (filtro.isEmpty()) {
            mostrarMsg("Ingrese un nombre");
            return;
        }
        List<Paciente> candidatos = pacienteService.leerTodos().stream()
                .filter(p -> p.getNombre().toLowerCase().contains(filtro))
                .collect(Collectors.toList());
        if (candidatos.isEmpty()) {
            mostrarMsg("Sin coincidencias");
            return;
        }
        if (candidatos.size() > 1) {
            String names = candidatos.stream().map(Paciente::getNombre).collect(Collectors.joining(", "));
            mostrarMsg("Múltiples: " + names);
            return;
        }
        Paciente p = candidatos.get(0);
        pacienteSeleccionadoId = p.getId();
        mostrarPaciente(p);
        cargarRecetasPaciente();
    }

    private void cargarRecetasPaciente() {
        if (pacienteSeleccionadoId == null) return;
        List<Receta> recetas = controller.obtenerRecetasPorPaciente(pacienteSeleccionadoId);
        recetaTableModel.setRecetas(recetas);
        recetaSeleccionadaId = null;
    }

    private void cambiarEstado() {
        if (recetaSeleccionadaId == null) {
            mostrarMsg("Seleccione una receta");
            return;
        }
        EstadoReceta nuevo = (EstadoReceta) estadoComboBox.getSelectedItem();
        if (nuevo == null) {
            mostrarMsg("Seleccione un estado");
            return;
        }
        controller.actualizarEstadoReceta(recetaSeleccionadaId, nuevo);
        cargarRecetasPaciente();
    }

    private void onTableSelection(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) return;
        int row = recetasTable.getSelectedRow();
        if (row < 0) {
            recetaSeleccionadaId = null;
            return;
        }
        Receta r = recetaTableModel.getRecetaAt(row);
        recetaSeleccionadaId = r.getId();
        try {
            estadoComboBox.setSelectedItem(r.getEstadoEnum());
        } catch (Exception ignored) {}
    }

    private void mostrarPaciente(Paciente p) {
        pacienteInfoLabel.setText("Paciente: " + p.getNombre() + " (ID " + p.getId() + ")");
        telefonoLabel.setText("Teléfono: " + p.getTelefono());
        fechaNacimientoLabel.setText("Nacimiento: " + (p.getFechanacimiento() != null ? new SimpleDateFormat("dd/MM/yyyy").format(p.getFechanacimiento()) : ""));
    }

    private void limpiar() {
        DatoPacienteField.setText("");
        pacienteInfoLabel.setText("Seleccione un paciente");
        telefonoLabel.setText("");
        fechaNacimientoLabel.setText("");
        pacienteSeleccionadoId = null;
        recetaSeleccionadaId = null;
        recetaTableModel.setRecetas(List.of());
    }

    private void mostrarMsg(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }

    // Generated component creation hook (if using custom buttons later)
    private void createUIComponents() {


        buscarButton = new BlueRoundedButton( "Buscar") ;
        limpiarButton = new BlueRoundedButton( "Limpiar") ;
        cambiarEstadoButton = new BlueRoundedButton( "Cambiar Estado") ;
    }
}
