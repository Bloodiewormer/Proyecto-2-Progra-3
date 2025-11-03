package org.example.Presentation.Views;

import org.example.Domain.Dtos.Paciente.PacienteResponseDto;
import org.example.Domain.Dtos.Receta.RecetaResponseDto;
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
    private JPanel actionPanel;

    private JTextField idPacienteField;
    private JButton buscarButton;
    private JButton limpiarButton;

    private JLabel pacienteInfoLabel;
    private JLabel telefonoLabel;
    private JLabel fechaNacimientoLabel;

    private JTable recetasTable;
    private JScrollPane scrollPane;

    private JComboBox<EstadoReceta> estadoComboBox;
    private JButton cambiarEstadoButton;
    private JButton refrescarButton;

    private final DespachoController controller;
    private RecetaTableModel recetaTableModel;
    private Integer pacienteSeleccionadoId;
    private Integer recetaSeleccionadaId;

    public DespachoForm(DespachoService despachoService, int idFarmaceuta) {
        this.controller = new DespachoController(this, despachoService, idFarmaceuta);

        setLayout(new BorderLayout());
        initComponents();
        initTable();
        initEstadoCombo();
        wireEvents();

        add(mainPanel, BorderLayout.CENTER);

        controller.cargarRecetas();
    }

    private void initComponents() {
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("ID Paciente:"));
        idPacienteField = new JTextField(10);
        searchPanel.add(idPacienteField);
        buscarButton = new JButton("Buscar");
        searchPanel.add(buscarButton);
        limpiarButton = new JButton("Limpiar");
        searchPanel.add(limpiarButton);
        refrescarButton = new JButton("Refrescar");
        searchPanel.add(refrescarButton);

        infoPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Información del Paciente"));
        pacienteInfoLabel = new JLabel("Paciente: N/A");
        telefonoLabel = new JLabel("Teléfono: N/A");
        fechaNacimientoLabel = new JLabel("Fecha Nac.: N/A");
        infoPanel.add(pacienteInfoLabel);
        infoPanel.add(telefonoLabel);
        infoPanel.add(fechaNacimientoLabel);

        tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Recetas"));
        recetasTable = new JTable();
        scrollPane = new JScrollPane(recetasTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actionPanel.add(new JLabel("Cambiar estado a:"));
        estadoComboBox = new JComboBox<>();
        actionPanel.add(estadoComboBox);
        cambiarEstadoButton = new JButton("Cambiar Estado");
        cambiarEstadoButton.setEnabled(false);
        actionPanel.add(cambiarEstadoButton);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(searchPanel, BorderLayout.NORTH);
        topPanel.add(infoPanel, BorderLayout.CENTER);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        mainPanel.add(actionPanel, BorderLayout.SOUTH);
    }

    private void initTable() {
        recetaTableModel = new RecetaTableModel();
        recetasTable.setModel(recetaTableModel);
        recetasTable.getSelectionModel().addListSelectionListener(this::onTableSelection);
        recetasTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void initEstadoCombo() {
        estadoComboBox.removeAllItems();
        estadoComboBox.addItem(EstadoReceta.PROCESO);
        estadoComboBox.addItem(EstadoReceta.LISTA);
        estadoComboBox.addItem(EstadoReceta.ENTREGADA);
    }

    private void wireEvents() {
        buscarButton.addActionListener(e -> controller.buscarPaciente());
        limpiarButton.addActionListener(e -> controller.limpiar());
        refrescarButton.addActionListener(e -> controller.cargarRecetas());
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

    public JPanel getMainPanel() { return mainPanel; }
    public JTextField getIdPacienteField() { return idPacienteField; }
    public JTable getRecetasTable() { return recetasTable; }
    public RecetaTableModel getRecetaTableModel() { return recetaTableModel; }
    public JComboBox<EstadoReceta> getEstadoComboBox() { return estadoComboBox; }
    public Integer getRecetaSeleccionadaId() { return recetaSeleccionadaId; }
    public Integer getPacienteSeleccionadoId() { return pacienteSeleccionadoId; }
    public void setPacienteSeleccionadoId(Integer id) { this.pacienteSeleccionadoId = id; }

    public void mostrarPaciente(PacienteResponseDto paciente) {
        if (paciente != null) {
            pacienteInfoLabel.setText("Paciente: " + paciente.getNombre());
            telefonoLabel.setText("Teléfono: " + paciente.getTelefono());
            fechaNacimientoLabel.setText("Fecha Nac.: " + paciente.getFechaNacimiento());
            pacienteSeleccionadoId = paciente.getId();
        }
    }

    public void limpiarPaciente() {
        pacienteInfoLabel.setText("Paciente: N/A");
        telefonoLabel.setText("Teléfono: N/A");
        fechaNacimientoLabel.setText("Fecha Nac.: N/A");
        pacienteSeleccionadoId = null;
        idPacienteField.setText("");
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}