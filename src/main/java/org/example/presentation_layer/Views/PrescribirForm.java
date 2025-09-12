package org.example.presentation_layer.Views;

import com.toedter.calendar.JDateChooser;
import org.example.domain_layer.DetalleReceta;
import org.example.domain_layer.Medicamento;
import org.example.domain_layer.Paciente;
import org.example.domain_layer.Receta;
import org.example.presentation_layer.Components.BlueRoundedButton;
import org.example.presentation_layer.Components.CustomTextField;
import org.example.presentation_layer.Models.DetalleRecetaTableModel;
import org.example.presentation_layer.Models.MedicamentoTableModel;
import org.example.presentation_layer.Models.PacienteTableModel;
import org.example.service_layer.MedicamentoService;
import org.example.service_layer.PacienteService;
import org.example.service_layer.RecetaService;
import org.example.service_layer.UsuarioService;
import org.example.utilities.EstadoReceta;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PrescribirForm {
    private JPanel mainPanel;
    private JPanel controlPanel;
    private JButton buscarMainMenuPacienteButton;
    private JButton agregarMedicamentoMainMenuButton;
    private JPanel RectaPanel;
    private JTable MedicamentoRecetatable;
    private JButton guardarMainMenuButton;
    private JButton detallesButton;
    private JButton limpiarMainMenuButton;
    private JButton descartarMainMenuButton;
    private JPanel BuscarPacienteLable;
    private JComboBox filtrarPacientecomboBox;
    private JTextField busquedaPacientetextField;
    private JTable pacientesTable;
    private JPanel AgregarMedicamento;
    private JComboBox FiltraMedicamentoComboBox;
    private JTextField BusquedaMedicamentoTextField;
    private JTable MedicamentosTable;
    private JSpinner catidadSpinner;
    private JSpinner DuracionSpinner;
    private JTextField textField1;
    private JButton guardarDetallesMenuButton;
    private JButton cancerlarButton;
    private JPanel DetallePanel;
    private JButton cancePacienteMenulButton;
    private JButton guardarPacienteMenuButton;
    private JButton cancelMedicamentoMenuButton;
    private JButton guardarMedicamentoMenuButton;
    private JPanel window;
    private JPanel DatePickerPanel;
    private JLabel PacientePromtPlace;
    private JButton buscarPacientMenuButton;
    private JButton buscarMedicamentoMenuButton;

    private JDateChooser DatePicker;
    private final List<DetalleReceta> detalleRecetaList = new ArrayList<>();

    private final PacienteService pacienteService;
    private final MedicamentoService medicamentoService;
    private final RecetaService recetaService;
    private Paciente selectedPaciente;
    private final int idMedico;

    // Dialog references (reuse)
    private JDialog buscarPacienteDialog;
    private JDialog agregarMedicamentoDialog;
    private JDialog detalleDialog;

    // Estado actual para detalle
    private Medicamento currentMedicamento;

    public PrescribirForm(UsuarioService usuarioService,
                          PacienteService pacienteService,
                          MedicamentoService medicamentoService,
                          RecetaService recetaService,
                          int idMedico) {
        this.pacienteService = pacienteService;
        this.medicamentoService = medicamentoService;
        this.recetaService = recetaService;
        this.idMedico = idMedico;

        MedicamentoRecetatable.setModel(new DetalleRecetaTableModel(detalleRecetaList));
        initDatePickers();
        MedicamentosTable.setModel(new MedicamentoTableModel(medicamentoService.leerTodos()));
        pacientesTable.setModel(new PacienteTableModel(pacienteService.leerTodos()));

        filtrarPacientecomboBox.setModel(new DefaultComboBoxModel<>(new String[] {"ID", "Nombre"}));
        FiltraMedicamentoComboBox.setModel(new DefaultComboBoxModel<>(new String[] {"ID", "Nombre"}));

        // Un solo listener de selección de paciente
        pacientesTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = pacientesTable.getSelectedRow();
                if (row != -1) {
                    PacienteTableModel model = (PacienteTableModel) pacientesTable.getModel();
                    int modelRow = pacientesTable.convertRowIndexToModel(row);
                    Paciente paciente = model.getPacienteAt(modelRow);
                    PacientePromtPlace.setText("Paciente: " + paciente.getNombre());
                }
            }
        });

        // Filtros paciente
        busquedaPacientetextField.getDocument().addDocumentListener(new SimpleDocListener(() -> filterPacientes()));
        filtrarPacientecomboBox.addActionListener(e -> filterPacientes());

        // Filtros medicamento
        BusquedaMedicamentoTextField.getDocument().addDocumentListener(new SimpleDocListener(() -> filterMedicamentos()));
        FiltraMedicamentoComboBox.addActionListener(e -> filterMedicamentos());

        // Main menu buttons
        buscarMainMenuPacienteButton.addActionListener(e -> showBuscarPacienteDialog());
        agregarMedicamentoMainMenuButton.addActionListener(e -> showAgregarMedicamentoDialog());

        limpiarMainMenuButton.addActionListener(e -> {
            detalleRecetaList.clear();
            ((DetalleRecetaTableModel) MedicamentoRecetatable.getModel()).fireTableDataChanged();
        });
        descartarMainMenuButton.addActionListener(e -> {
            detalleRecetaList.clear();
            ((DetalleRecetaTableModel) MedicamentoRecetatable.getModel()).fireTableDataChanged();
            selectedPaciente = null;
            PacientePromtPlace.setText("Paciente: ");
        });
        guardarMainMenuButton.addActionListener(e -> guardarReceta());
    }

    private void guardarReceta() {
        if (selectedPaciente == null || detalleRecetaList.isEmpty()) {
            JOptionPane.showMessageDialog(mainPanel,
                    "Seleccione un paciente y agregue al menos un medicamento.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        int newId = recetaService.getNextId();
        Date fechaRetiro = DatePicker.getDate();
        Receta receta = new Receta(newId,
                selectedPaciente.getId(),
                idMedico,
                new Date(),
                fechaRetiro,
                EstadoReceta.PROCESO.toString());
        receta.setDetalles(new ArrayList<>(detalleRecetaList));
        recetaService.agregar(receta);
        JOptionPane.showMessageDialog(mainPanel, "Receta guardada.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        detalleRecetaList.clear();
        ((DetalleRecetaTableModel) MedicamentoRecetatable.getModel()).fireTableDataChanged();
        selectedPaciente = null;
        PacientePromtPlace.setText("Paciente: ");
    }
    // ---------------- Dialog Builders ----------------

    private void showBuscarPacienteDialog() {
        if (buscarPacienteDialog == null) {
            buscarPacienteDialog = buildBaseDialog("Buscar Paciente", BuscarPacienteLable);
            buscarPacientMenuButton.addActionListener(e -> filterPacientes());
            guardarPacienteMenuButton.addActionListener(e -> {
                int row = pacientesTable.getSelectedRow();
                if (row != -1) {
                    PacienteTableModel model = (PacienteTableModel) pacientesTable.getModel();
                    int modelRow = pacientesTable.convertRowIndexToModel(row);
                    selectedPaciente = model.getPacienteAt(modelRow);
                    PacientePromtPlace.setText("Paciente: " + selectedPaciente.getNombre());
                }
                buscarPacienteDialog.setVisible(false);
            });
            cancePacienteMenulButton.addActionListener(e -> buscarPacienteDialog.setVisible(false));
        }
        buscarPacienteDialog.setLocationRelativeTo(mainPanel);
        buscarPacienteDialog.setVisible(true);
    }

    private void showAgregarMedicamentoDialog() {
        if (agregarMedicamentoDialog == null) {
            agregarMedicamentoDialog = buildBaseDialog("Agregar Medicamento", AgregarMedicamento);
            buscarMedicamentoMenuButton.addActionListener(e -> filterMedicamentos());
            guardarMedicamentoMenuButton.addActionListener(e -> {
                Medicamento m = getSelectedMedicamento();
                if (m == null) {
                    JOptionPane.showMessageDialog(agregarMedicamentoDialog,
                            "Seleccione un medicamento.",
                            "Advertencia",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }
                showDetalleDialog(m);
                agregarMedicamentoDialog.setVisible(false);
            });
            cancelMedicamentoMenuButton.addActionListener(e -> agregarMedicamentoDialog.setVisible(false));
        }
        agregarMedicamentoDialog.setLocationRelativeTo(mainPanel);
        agregarMedicamentoDialog.setVisible(true);
    }

    private void showDetalleDialog(Medicamento medicamento) {
        currentMedicamento = medicamento;
        if (detalleDialog == null) {
            detalleDialog = buildBaseDialog("Detalles", DetallePanel);
            guardarDetallesMenuButton.addActionListener(e -> {
                if (currentMedicamento == null) {
                    detalleDialog.setVisible(false);
                    return;
                }
                int cantidad = (Integer) catidadSpinner.getValue();
                int duracion = (Integer) DuracionSpinner.getValue();
                String indicaciones = textField1.getText();
                detalleRecetaList.add(new DetalleReceta(currentMedicamento, cantidad, indicaciones, duracion));
                ((DetalleRecetaTableModel) MedicamentoRecetatable.getModel()).fireTableDataChanged();
                detalleDialog.setVisible(false);
                // Reset referencia (opcional)
                currentMedicamento = null;
            });
            cancerlarButton.addActionListener(e -> {
                currentMedicamento = null;
                detalleDialog.setVisible(false);
            });
        }
        // Reset campos
        catidadSpinner.setValue(1);
        DuracionSpinner.setValue(1);
        textField1.setText("");
        detalleDialog.setLocationRelativeTo(mainPanel);
        detalleDialog.setVisible(true);
    }

    private JDialog buildBaseDialog(String title, JPanel content) {
        JDialog d = new JDialog(SwingUtilities.getWindowAncestor(mainPanel), title, Dialog.ModalityType.APPLICATION_MODAL);
        d.setContentPane(content);
        d.pack();
        d.setSize(500, 300);
        return d;
    }





    // ---------------- Filtros ----------------

    private void filterPacientes() {
        String criterio = (String) filtrarPacientecomboBox.getSelectedItem();
        String texto = busquedaPacientetextField.getText().trim();
        List<Paciente> filtrados = new ArrayList<>();
        if ("ID".equals(criterio)) {
            try {
                int id = Integer.parseInt(texto);
                Paciente p = pacienteService.leerPorId(id);
                if (p != null) filtrados.add(p);
            } catch (NumberFormatException ignored) {}
        } else {
            filtrados = pacienteService.buscarPorNombre(texto);
        }
        pacientesTable.setModel(new PacienteTableModel(filtrados));
    }

    private void filterMedicamentos() {
        String criterio = (String) FiltraMedicamentoComboBox.getSelectedItem();
        String texto = BusquedaMedicamentoTextField.getText().trim();
        List<Medicamento> filtrados = new ArrayList<>();
        if ("ID".equals(criterio)) {
            try {
                int id = Integer.parseInt(texto);
                Medicamento m = medicamentoService.leerPorId(id);
                if (m != null) filtrados.add(m);
            } catch (NumberFormatException ignored) {}
        } else {
            filtrados = medicamentoService.buscarPorNombre(texto);
        }
        MedicamentosTable.setModel(new MedicamentoTableModel(filtrados));
    }

    private void initDatePickers() {
        DatePickerPanel.setLayout(new BorderLayout());
        DatePicker = new JDateChooser();
        DatePicker.setDate(new Date());
        DatePickerPanel.add(DatePicker, BorderLayout.CENTER);
    }


    public JPanel getMainPanel() {
        return mainPanel;
    }

    private Medicamento getSelectedMedicamento() {
        int selectedRow = MedicamentosTable.getSelectedRow();
        if (selectedRow == -1) return null;
        MedicamentoTableModel model = (MedicamentoTableModel) MedicamentosTable.getModel();
        int modelRow = MedicamentosTable.convertRowIndexToModel(selectedRow);
        return model.getMedicamentoAt(modelRow);
    }

    private void createUIComponents() {
        buscarMainMenuPacienteButton = new BlueRoundedButton("Buscar Paciente");
        agregarMedicamentoMainMenuButton = new BlueRoundedButton("Agregar Medicamento");
        guardarMainMenuButton = new BlueRoundedButton("Guardar");
        detallesButton = new BlueRoundedButton("Detalles");
        limpiarMainMenuButton = new BlueRoundedButton("Limpiar");
        descartarMainMenuButton = new BlueRoundedButton("Descartar");
        guardarDetallesMenuButton = new BlueRoundedButton("Guardar");
        cancerlarButton = new BlueRoundedButton("Cancelar");
        cancePacienteMenulButton = new BlueRoundedButton("Cancelar");
        guardarPacienteMenuButton = new BlueRoundedButton("Guardar");
        cancelMedicamentoMenuButton = new BlueRoundedButton("Cancelar");
        guardarMedicamentoMenuButton = new BlueRoundedButton("Guardar");
        busquedaPacientetextField = new CustomTextField();
        BusquedaMedicamentoTextField = new CustomTextField();
    }


    //se usa para los document listener
    private static class SimpleDocListener implements javax.swing.event.DocumentListener {
        private final Runnable r;
        SimpleDocListener(Runnable r) { this.r = r; }
        public void insertUpdate(javax.swing.event.DocumentEvent e) { r.run(); }
        public void removeUpdate(javax.swing.event.DocumentEvent e) { r.run(); }
        public void changedUpdate(javax.swing.event.DocumentEvent e) { r.run(); }
    }


}
