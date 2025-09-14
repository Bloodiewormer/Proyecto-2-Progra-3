package org.example.presentation_layer.Views;

import org.example.domain_layer.Paciente;
import org.example.domain_layer.Receta;
import org.example.presentation_layer.Components.BlueRoundedButton;
import org.example.presentation_layer.Controllers.HistoricoRecetasController;
import org.example.presentation_layer.Models.RecetaTableModel;
import org.example.service_layer.MedicamentoService;
import org.example.service_layer.PacienteService;
import org.example.service_layer.RecetaService;
import org.example.service_layer.UsuarioService;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

public class HistoricoRecetasView extends JPanel {
    private JPanel mainPanel;
    private JPanel searchPanel;
    private JComboBox<String> comboBusqueda;
    private JTextField DatoPacienteField;
    private JButton buscarButton;
    private JButton DetallesButton;
    private JPanel infoPanel;
    private JLabel pacienteInfoLabel;
    private JLabel telefonoLabel;
    private JLabel fechaNacimientoLabel;
    private JPanel tablePanel;
    private JScrollPane scrollPane;
    private JTable recetasTable;

    private final HistoricoRecetasController controller;

    private final PacienteService pacienteService;
    private final RecetaService recetaService;
    private final MedicamentoService medicamentoService;
    private final UsuarioService medicoService;


    private RecetaTableModel recetaTableModel;
    private Integer pacienteSeleccionadoId;
    private Integer recetaSeleccionadaId;

    public HistoricoRecetasView(HistoricoRecetasController controller,
                                PacienteService pacienteService,
                                RecetaService recetaService,
                                MedicamentoService medicamentoService,
                                UsuarioService medicoService) {
        this.controller = controller;
        this.pacienteService = pacienteService;
        this.recetaService = recetaService;
        this.medicamentoService = medicamentoService;
        this.medicoService = medicoService;
        initUI();
        initTable();
        wireEvents();
    }

    private void initUI() {
        setSize(700, 400);
        // Panel búsqueda
        comboBusqueda.setModel(new DefaultComboBoxModel<>(new String[]{"ID", "Nombre"}));


    }
    public JPanel getMainPanel() {
        return mainPanel;
    }

    private void initTable() {
        recetaTableModel = new RecetaTableModel(controller.obtenerRecetas());
        recetasTable.setModel(recetaTableModel);
        recetasTable.getSelectionModel().addListSelectionListener(this::onTableSelection);
    }



    private void wireEvents() {
        buscarButton.addActionListener(e -> buscarPaciente());
        DetallesButton.addActionListener(e -> mostrarDetallesReceta());
    }

    private void mostrarDetallesReceta() {
        if (recetaSeleccionadaId == null) {
            mostrarMsg("Seleccione una receta");
            return;
        }
        Receta receta = recetaTableModel.getRecetaById(recetaSeleccionadaId);
        if (receta == null) {
            mostrarMsg("Receta no encontrada");
            return;
        }

        // Get patient info
        var paciente = pacienteService.leerPorId(receta.getIdPaciente());
        String pacienteInfo = (paciente != null)
                ? paciente.getNombre() + " (ID " + paciente.getId() + "), Tel: " + paciente.getTelefono() +
                ", Nacimiento: " + (paciente.getFechaNacimiento() != null ? new SimpleDateFormat("dd/MM/yyyy").format(paciente.getFechaNacimiento()) : "")
                : "Desconocido";

        // Get doctor info
        var medico = medicoService.leerPorId(receta.getIdMedico());
        String medicoInfo;
        if (medico != null) {
            medicoInfo = medico.getNombre() + " (ID " + medico.getId() + "), Usuario: " + medico.getNombre();
        }
        else {
            medicoInfo = "Desconocido";
        }


        StringBuilder detalles = new StringBuilder();
        detalles.append("ID Receta: ").append(receta.getId()).append("\n");
        detalles.append("Paciente: ").append(pacienteInfo).append("\n");
        detalles.append("Médico: ").append(medicoInfo).append("\n");
        detalles.append("Fecha Confección: ").append(receta.getFechaConfeccion()).append("\n");
        detalles.append("Fecha Retiro: ").append(receta.getFechaRetiro()).append("\n");
        detalles.append("Estado: ").append(receta.getEstado()).append("\n\n");
        detalles.append("Detalles:\n");

        for (var d : receta.getDetalles()) {
            detalles.append("- Medicamento: ").append(d.getMedicamento().getNombre())
                    .append(", Cantidad: ").append(d.getCantidad())
                    .append(", Indicaciones: ").append(d.getIndicaciones())
                    .append(", Días: ").append(d.getDias())
                    .append("\n");
        }

        JTextArea textArea = new JTextArea(detalles.toString());
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new java.awt.Dimension(400, 300));

        JOptionPane.showMessageDialog(this, scrollPane, "Detalles de Receta", JOptionPane.INFORMATION_MESSAGE);
    }


    private void buscarPaciente() {
        String tipo = (String) comboBusqueda.getSelectedItem();
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


    private void onTableSelection(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) return;
        int row = recetasTable.getSelectedRow();
        if (row < 0) {
            recetaSeleccionadaId = null;
            return;
        }
        Receta r = recetaTableModel.getRecetaAt(row);
        recetaSeleccionadaId = r.getId();
    }

    private void mostrarPaciente(Paciente p) {
        pacienteInfoLabel.setText("Paciente: " + p.getNombre() + " (ID " + p.getId() + ")");
        telefonoLabel.setText("Teléfono: " + p.getTelefono());
        fechaNacimientoLabel.setText("Nacimiento: " + (p.getFechaNacimiento() != null ? new SimpleDateFormat("dd/MM/yyyy").format(p.getFechaNacimiento()) : ""));
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


    private void createUIComponents() {
        DetallesButton = new BlueRoundedButton("Historico Recetas");
        buscarButton = new BlueRoundedButton("Historico Recetas");


    }








//    private void buscarPaciente() {
//        String criterio = (String) comboBusqueda.getSelectedItem();
//        String valor = txtBusqueda.getText().trim();
//
//        if (valor.isEmpty()) {
//            JOptionPane.showMessageDialog(this, "Ingrese un valor de búsqueda", "Error", JOptionPane.ERROR_MESSAGE);
//            return;
//        }
//
//        Paciente paciente = null;
//
//        try {
//            if ("Buscar por ID".equals(criterio)) {
//                int id = Integer.parseInt(valor);
//                paciente = controller.buscarPacientePorId(id);
//            } else {
//                List<Paciente> pacientes = controller.buscarPacientePorNombre(valor);
//                if (pacientes.size() == 1) {
//                    paciente = pacientes.get(0);
//                } else if (pacientes.size() > 1) {
//                    JOptionPane.showMessageDialog(this, "Se encontraron varios pacientes con ese nombre", "Atención", JOptionPane.INFORMATION_MESSAGE);
//                    return;
//                }
//            }
//        } catch (NumberFormatException ex) {
//            JOptionPane.showMessageDialog(this, "El ID debe ser un número válido", "Error", JOptionPane.ERROR_MESSAGE);
//            return;
//        }
//
//        if (paciente == null) {
//            JOptionPane.showMessageDialog(this, "Paciente no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
//            return;
//        }
//
//        mostrarHistorial(paciente);
//    }
//
//    private void mostrarHistorial(Paciente paciente) {
//        modeloTabla.setRowCount(0);
//        List<Receta> recetas = controller.obtenerHistorialRecetas(paciente);
//
//        for (Receta r : recetas) {
//            modeloTabla.addRow(new Object[]{
//                    r.getId(),
//                    r.getFecha(),
//                    r.getEstado(),
//                    r.getMedicamentos().toString()
//            });
//        }
//    }


}
