package org.example.Presentation.Views;

import org.example.Domain.Dtos.Paciente.PacienteResponseDto;
import org.example.Domain.Dtos.Receta.RecetaResponseDto;
import org.example.Presentation.Components.BlueRoundedButton;
import org.example.Presentation.Components.LoadingOverlay;
import org.example.Presentation.Controllers.DespachoController;
import org.example.Presentation.Models.RecetaTableModel;
import org.example.Services.DespachoService;
import org.example.Utilities.EstadoReceta;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;

public class DespachoForm extends JPanel {
    private JPanel mainPanel;
    private JPanel searchPanel;
    private JPanel infoPanel;
    private JPanel tablePanel;
    private JPanel estadoPanel;

    private JTextField DatoPacienteField;
    private JButton buscarButton;
    private JButton limpiarButton;

    private JLabel pacienteInfoLabel;
    private JLabel telefonoLabel;
    private JLabel fechaNacimientoLabel;

    private JTable recetasTable;
    private JScrollPane scrollPane;

    private JComboBox<EstadoReceta> estadoComboBox;
    private JButton cambiarEstadoButton;
    private JComboBox<String> searchBycomboBox;

    private final DespachoController controller;
    private final RecetaTableModel recetaTableModel;
    private final LoadingOverlay loadingOverlay;
    private Integer pacienteSeleccionadoId;
    private Integer recetaSeleccionadaId;

    public DespachoForm(DespachoService despachoService, int idFarmaceuta, JFrame parentFrame) {
        this.controller = new DespachoController(this, despachoService, idFarmaceuta);
        this.recetaTableModel = new RecetaTableModel();
        this.loadingOverlay = new LoadingOverlay(parentFrame);

        setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);

        initComponents();
        wireEvents();

        controller.cargarRecetas();
    }

    private void initComponents() {
        searchBycomboBox.setModel(new DefaultComboBoxModel<>(new String[]{"ID", "Nombre"}));

        recetasTable.setModel(recetaTableModel);
        recetasTable.getSelectionModel().addListSelectionListener(this::onTableSelection);
        recetasTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        estadoComboBox.removeAllItems();
        for (EstadoReceta e : EstadoReceta.values()) {
            estadoComboBox.addItem(e);
        }
    }

    private void wireEvents() {
        buscarButton.addActionListener(e -> controller.buscarPaciente());
        limpiarButton.addActionListener(e -> controller.limpiar());
        cambiarEstadoButton.addActionListener(e -> controller.cambiarEstado());
    }

    private void onTableSelection(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            int selectedRow = recetasTable.getSelectedRow();
            if (selectedRow >= 0) {
                RecetaResponseDto receta = recetaTableModel.getRecetaAt(selectedRow);
                if (receta != null) {
                    recetaSeleccionadaId = receta.getId();
                    cambiarEstadoButton.setEnabled(true);
                    configurarEstadosValidos(receta.getEstado());
                }
            } else {
                recetaSeleccionadaId = null;
                cambiarEstadoButton.setEnabled(false);
            }
        }
    }

    private void configurarEstadosValidos(String estadoActual) {
        estadoComboBox.removeAllItems();
        try {
            EstadoReceta estado = EstadoReceta.valueOf(estadoActual);
            switch (estado) {
                case CONFECCIONADA -> estadoComboBox.addItem(EstadoReceta.PROCESO);
                case PROCESO -> {
                    estadoComboBox.addItem(EstadoReceta.LISTA);
                    estadoComboBox.addItem(EstadoReceta.CONFECCIONADA);
                }
                case LISTA -> {
                    estadoComboBox.addItem(EstadoReceta.ENTREGADA);
                    estadoComboBox.addItem(EstadoReceta.PROCESO);
                }
                case ENTREGADA -> cambiarEstadoButton.setEnabled(false);
            }
        } catch (Exception ex) {
            System.err.println("Estado desconocido: " + estadoActual);
        }
    }

    public void showLoading(boolean visible) {
        loadingOverlay.show(visible);
    }

    public void mostrarPaciente(PacienteResponseDto paciente) {
        if (paciente != null) {
            pacienteInfoLabel.setText("Paciente: " + paciente.getNombre());
            telefonoLabel.setText("Teléfono: " + paciente.getTelefono());
            fechaNacimientoLabel.setText("Nacimiento: " + paciente.getFechaNacimiento());
            pacienteSeleccionadoId = paciente.getId();
        }
    }

    public void limpiarPaciente() {
        pacienteInfoLabel.setText("Seleccione un paciente");
        telefonoLabel.setText("");
        fechaNacimientoLabel.setText("");
        pacienteSeleccionadoId = null;
        DatoPacienteField.setText("");
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    // Getters
    public JPanel getMainPanel() { return mainPanel; }
    public RecetaTableModel getRecetaTableModel() { return recetaTableModel; }
    public JTable getRecetasTable() { return recetasTable; }
    public JTextField getDatoPacienteField() { return DatoPacienteField; }
    public JComboBox<String> getSearchByComboBox() { return searchBycomboBox; }
    public JComboBox<EstadoReceta> getEstadoComboBox() { return estadoComboBox; }
    public Integer getRecetaSeleccionadaId() { return recetaSeleccionadaId; }
    public Integer getPacienteSeleccionadoId() { return pacienteSeleccionadoId; }

    private void createUIComponents() {
        buscarButton = new BlueRoundedButton("Buscar");
        limpiarButton = new BlueRoundedButton("Limpiar");
        cambiarEstadoButton = new BlueRoundedButton("Cambiar Estado");
    }


}
