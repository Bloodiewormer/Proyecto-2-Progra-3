package org.example.presentation_layer.Views;

import org.example.domain_layer.Paciente;
import org.example.domain_layer.Receta;
import org.example.presentation_layer.Controllers.DespachoController;
import org.example.service_layer.PacienteService;
import org.example.service_layer.RecetaService;
import org.example.service_layer.MedicamentoService;
import org.example.utilities.EstadoReceta;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class DespachoForm extends JPanel {

    // Componentes del formulario (generados automáticamente por el UI Designer)
    private JPanel MainPanel;
    private JPanel searchPanel;
    private JPanel infoPanel;
    private JPanel tablePanel;
    private JPanel estadoPanel;

    private JTextField idPacienteField;
    private JTextField nombrePacienteField;
    private JButton buscarPorIdButton;
    private JButton buscarPorNombreButton;
    private JButton limpiarButton;

    private JLabel pacienteInfoLabel;
    private JLabel telefonoLabel;
    private JLabel fechaNacimientoLabel;

    private JTable recetasTable;
    private JScrollPane scrollPane;
    private JComboBox<EstadoReceta> estadoComboBox;
    private JButton cambiarEstadoButton;

    // Controladores y servicios
    private DespachoController despachoController;
    private PacienteService pacienteService;
    private RecetaService recetaService;
    private MedicamentoService medicamentoService;

    // Modelo de la tabla
    private DefaultTableModel tableModel;

    // Paciente actual
    private Paciente pacienteActual;

    public DespachoForm(PacienteService pacienteService, RecetaService recetaService, MedicamentoService medicamentoService) {
        this.pacienteService = pacienteService;
        this.recetaService = recetaService;
        this.medicamentoService = medicamentoService;

        // Inicializar controlador
        this.despachoController = new DespachoController(recetaService, medicamentoService);

        // Configurar la ventana
       //setContentPane(MainPanel);
        setTitle("Despacho de Medicamentos");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        // Inicializar componentes
        inicializarComponentes();
        configurarEventos();
    }

    private void inicializarComponentes() {
        // Configurar ComboBox de estados
        DefaultComboBoxModel<EstadoReceta> comboModel = new DefaultComboBoxModel<>(EstadoReceta.values());
        estadoComboBox.setModel(comboModel);

        // Configurar tabla
        String[] columnNames = {"ID Receta", "Fecha Confección", "Fecha Retiro", "Estado", "Médico ID", "# Medicamentos"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        recetasTable.setModel(tableModel);

        // Configurar apariencia
        recetasTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        recetasTable.getTableHeader().setBackground(new Color(0, 74, 173));
        recetasTable.getTableHeader().setForeground(Color.WHITE);
        recetasTable.setRowHeight(25);

        // Configurar botones con colores
        configurarEstiloBotones();

        // Estado inicial
        limpiarInformacion();
    }

    private void configurarEstiloBotones() {
        // Botones azules
        buscarPorIdButton.setBackground(new Color(0, 74, 173));
        buscarPorIdButton.setForeground(Color.WHITE);
        buscarPorNombreButton.setBackground(new Color(0, 74, 173));
        buscarPorNombreButton.setForeground(Color.WHITE);
        cambiarEstadoButton.setBackground(new Color(0, 74, 173));
        cambiarEstadoButton.setForeground(Color.WHITE);

        // Botón gris
        limpiarButton.setBackground(new Color(108, 117, 125));
        limpiarButton.setForeground(Color.WHITE);
    }

    private void configurarEventos() {
        // Botones de búsqueda
        buscarPorIdButton.addActionListener(e -> buscarPacientePorId());
        buscarPorNombreButton.addActionListener(e -> buscarPacientePorNombre());
        limpiarButton.addActionListener(e -> limpiarCampos());
        cambiarEstadoButton.addActionListener(e -> cambiarEstadoReceta());

        // Enter en campos de texto
        idPacienteField.addActionListener(e -> buscarPacientePorId());
        nombrePacienteField.addActionListener(e -> buscarPacientePorNombre());

        // Selección en tabla
        recetasTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = recetasTable.getSelectedRow();
                cambiarEstadoButton.setEnabled(selectedRow >= 0);
            }
        });
    }

    private void buscarPacientePorId() {
        try {
            String idText = idPacienteField.getText().trim();
            if (idText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor ingrese un ID de paciente");
                return;
            }

            int id = Integer.parseInt(idText);
            Paciente paciente = pacienteService.leerPorId(id);

            if (paciente != null) {
                pacienteActual = paciente;
                mostrarInformacionPaciente(paciente);
                cargarRecetasPaciente(id);
                nombrePacienteField.setText(paciente.getNombre());
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró paciente con ID: " + id);
                limpiarInformacion();
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor ingrese un ID válido (número)");
        }
    }

    private void buscarPacientePorNombre() {
        String nombre = nombrePacienteField.getText().trim();
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor ingrese un nombre");
            return;
        }

        List<Paciente> pacientesEncontrados = pacienteService.leerTodos().stream()
                .filter(p -> p.getNombre() != null && p.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                .collect(Collectors.toList());

        if (pacientesEncontrados.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No se encontraron pacientes con nombre: " + nombre);
            limpiarInformacion();
        } else if (pacientesEncontrados.size() == 1) {
            Paciente paciente = pacientesEncontrados.get(0);
            pacienteActual = paciente;
            mostrarInformacionPaciente(paciente);
            cargarRecetasPaciente(paciente.getId());
            idPacienteField.setText(String.valueOf(paciente.getId()));
        } else {
            mostrarSeleccionMultiple(pacientesEncontrados);
        }
    }

    private void mostrarSeleccionMultiple(List<Paciente> pacientes) {
        String[] opciones = pacientes.stream()
                .map(p -> String.format("%d - %s", p.getId(), p.getNombre()))
                .toArray(String[]::new);

        String seleccion = (String) JOptionPane.showInputDialog(
                this,
                "Se encontraron múltiples pacientes. Seleccione uno:",
                "Seleccionar Paciente",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]
        );

        if (seleccion != null) {
            int id = Integer.parseInt(seleccion.split(" - ")[0]);
            Paciente paciente = pacientes.stream()
                    .filter(p -> p.getId() == id)
                    .findFirst()
                    .orElse(null);

            if (paciente != null) {
                pacienteActual = paciente;
                mostrarInformacionPaciente(paciente);
                cargarRecetasPaciente(id);
                idPacienteField.setText(String.valueOf(id));
                nombrePacienteField.setText(paciente.getNombre());
            }
        }
    }

    private void mostrarInformacionPaciente(Paciente paciente) {
        pacienteInfoLabel.setText(String.format("Paciente: %s (ID: %d)",
                paciente.getNombre(), paciente.getId()));

        telefonoLabel.setText("Teléfono: " +
                (paciente.getTelefono() != null ? paciente.getTelefono() : "No especificado"));

        if (paciente.getFechanacimiento() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            fechaNacimientoLabel.setText("Fecha de Nacimiento: " +
                    paciente.getFechanacimiento().toInstant()
                            .atZone(java.time.ZoneId.systemDefault())
                            .toLocalDate()
                            .format(formatter));
        } else {
            fechaNacimientoLabel.setText("Fecha de Nacimiento: No especificada");
        }
    }

    private void cargarRecetasPaciente(int pacienteId) {
        // Limpiar tabla
        tableModel.setRowCount(0);

        // Obtener recetas del paciente
        List<Receta> recetasPaciente = despachoController.obtenerRecetasPorPaciente(pacienteId);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Agregar recetas a la tabla
        for (Receta receta : recetasPaciente) {
            String fechaConfeccion = receta.getFechaConfeccion() != null ?
                    receta.getFechaConfeccion().format(formatter) : "N/A";
            String fechaRetiro = receta.getFechaRetiro() != null ?
                    receta.getFechaRetiro().format(formatter) : "N/A";

            int cantidadMedicamentos = receta.getDetalles() != null ? receta.getDetalles().size() : 0;

            Object[] row = {
                    receta.getId(),
                    fechaConfeccion,
                    fechaRetiro,
                    receta.getEstado(),
                    receta.getIdMedico(),
                    cantidadMedicamentos
            };
            tableModel.addRow(row);
        }

        // Mostrar información
        if (recetasPaciente.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No se encontraron recetas para este paciente");
        }

        cambiarEstadoButton.setEnabled(false);
    }

    private void cambiarEstadoReceta() {
        int selectedRow = recetasTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Por favor seleccione una receta de la tabla");
            return;
        }

        int recetaId = (Integer) tableModel.getValueAt(selectedRow, 0);
        EstadoReceta nuevoEstado = (EstadoReceta) estadoComboBox.getSelectedItem();

        if (nuevoEstado == null) {
            JOptionPane.showMessageDialog(this, "Por favor seleccione un estado válido");
            return;
        }

        try {
            despachoController.actualizarEstadoReceta(recetaId, nuevoEstado);
            tableModel.setValueAt(nuevoEstado.toString(), selectedRow, 3);
            JOptionPane.showMessageDialog(this,
                    String.format("Estado de la receta %d cambiado a: %s", recetaId, nuevoEstado));

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cambiar el estado: " + ex.getMessage());
        }
    }

    private void limpiarCampos() {
        idPacienteField.setText("");
        nombrePacienteField.setText("");
        limpiarInformacion();
    }

    private void limpiarInformacion() {
        pacienteActual = null;
        pacienteInfoLabel.setText("Seleccione un paciente");
        telefonoLabel.setText("");
        fechaNacimientoLabel.setText("");
        tableModel.setRowCount(0);
        cambiarEstadoButton.setEnabled(false);
    }

    // Getters
    public JPanel getMainPanel() {
        return MainPanel;
    }

    public Paciente getPacienteActual() {
        return pacienteActual;
    }
}