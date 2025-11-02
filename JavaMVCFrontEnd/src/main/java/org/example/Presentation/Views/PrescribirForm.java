package org.example.Presentation.Views;

import com.toedter.calendar.JDateChooser;
import org.example.Domain.Dtos.DetalleReceta.DetalleRecetaResponseDto;
import org.example.Domain.Dtos.Paciente.PacienteResponseDto;
import org.example.Domain.Dtos.Medicamento.MedicamentoResponseDto;

import org.example.Presentation.Components.BlueRoundedButton;
import org.example.Presentation.Components.CustomTextField;
import org.example.Presentation.Controllers.PrescribirController;
import org.example.Presentation.Models.DetalleRecetaTableModel;
import org.example.Presentation.Models.MedicamentoTableModel;
import org.example.Presentation.Models.PacienteTableModel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PrescribirForm extends JPanel {

    private JPanel mainPanel;
    private JPanel controlPanel;
    private JButton buscarMainMenuPacienteButton;
    private JButton agregarMedicamentoMainMenuButton;
    private JPanel rectaPanel;
    private JTable MedicamentoRecetatable;
    private JButton guardarMainMenuButton;
    private JButton detallesButton;
    private JButton limpiarMainMenuButton;
    private JButton descartarMainMenuButton;

    private JPanel buscarPatientPanel;
    private JComboBox<String> filtrarPacientecomboBox;
    private JTextField busquedaPacientetextField;
    private JTable pacientesTable;

    private JPanel agregarMedicamentoPanel;
    private JComboBox<String> FiltraMedicamentoComboBox;
    private JTextField BusquedaMedicamentoTextField;
    private JTable MedicamentosTable;

    private JSpinner catidadSpinner;
    private JSpinner duracionSpinner;
    private JTextField textField1; // Indicaciones
    private JButton guardarDetallesMenuButton;
    private JButton cancerlarButton;

    private JPanel detallePanel;
    private JButton cancePacienteMenulButton;
    private JButton guardarPacienteMenuButton;
    private JButton cancelMedicamentoMenuButton;
    private JButton guardarMedicamentoMenuButton;

    private JPanel rootPanel;
    private JPanel datePickerPanel;
    private JLabel pacientePromtPlace;
    private JButton buscarPacientMenuButton;
    private JButton buscarMedicamentoMenuButton;

    private JDateChooser DatePicker;

    private final List<DetalleRecetaResponseDto> detalleRecetaList = new ArrayList<>();
    private PacienteResponseDto selectedPaciente;
    private MedicamentoResponseDto currentMedicamento;

    private JDialog buscarPacienteDialog;
    private JDialog agregarMedicamentoDialog;
    private JDialog detalleDialog;

    public PrescribirForm(JFrame parentFrame) {
        MedicamentoRecetatable.setModel(new DetalleRecetaTableModel());
        initDatePicker();
        MedicamentosTable.setModel(new MedicamentoTableModel());
        pacientesTable.setModel(new PacienteTableModel());

        catidadSpinner.setModel(new SpinnerNumberModel(1, 1, 1000, 1));
        duracionSpinner.setModel(new SpinnerNumberModel(1, 1, 365, 1));

        filtrarPacientecomboBox.setModel(new DefaultComboBoxModel<>(new String[]{"ID", "Nombre"}));
        FiltraMedicamentoComboBox.setModel(new DefaultComboBoxModel<>(new String[]{"ID", "Nombre"}));
    }

    // --- Getters for Controller ---
    public JPanel getMainPanel() { return mainPanel; }
    public JButton getBuscarMainMenuPacienteButton() { return buscarMainMenuPacienteButton; }
    public JButton getAgregarMedicamentoMainMenuButton() { return agregarMedicamentoMainMenuButton; }
    public JButton getGuardarMainMenuButton() { return guardarMainMenuButton; }
    public JButton getLimpiarMainMenuButton() { return limpiarMainMenuButton; }
    public JButton getDescartarMainMenuButton() { return descartarMainMenuButton; }
    public JButton getGuardarDetallesMenuButton() { return guardarDetallesMenuButton; }
    public JButton getCancerlarButton() { return cancerlarButton; }
    public JButton getGuardarPacienteMenuButton() { return guardarPacienteMenuButton; }
    public JButton getCancePacienteMenulButton() { return cancePacienteMenulButton; }
    public JButton getGuardarMedicamentoMenuButton() { return guardarMedicamentoMenuButton; }
    public JButton getCancelMedicamentoMenuButton() { return cancelMedicamentoMenuButton; }
    public JButton getBuscarPacientMenuButton() { return buscarPacientMenuButton; }
    public JButton getBuscarMedicamentoMenuButton() { return buscarMedicamentoMenuButton; }
    public JTable getPacientesTable() { return pacientesTable; }
    public JTable getMedicamentosTable() { return MedicamentosTable; }
    public JTable getMedicamentoRecetatable() { return MedicamentoRecetatable; }
    public JTextField getBusquedaPacientetextField() { return busquedaPacientetextField; }
    public JTextField getBusquedaMedicamentoTextField() { return BusquedaMedicamentoTextField; }
    public JComboBox<String> getFiltrarPacientecomboBox() { return filtrarPacientecomboBox; }
    public JComboBox<String> getFiltraMedicamentoComboBox() { return FiltraMedicamentoComboBox; }
    public JSpinner getCatidadSpinner() { return catidadSpinner; }
    public JSpinner getDuracionSpinner() { return duracionSpinner; }
    public JTextField getIndicacionesField() { return textField1; }
    public JLabel getPacientePromtPlace() { return pacientePromtPlace; }
    public JDateChooser getDatePicker() { return DatePicker; }
    public List<DetalleRecetaResponseDto> getDetalleRecetaList() { return detalleRecetaList; }
    public MedicamentoResponseDto getCurrentMedicamento() { return currentMedicamento; }
    public void setCurrentMedicamento(MedicamentoResponseDto m) { this.currentMedicamento = m; }
    public PacienteResponseDto getSelectedPaciente() { return selectedPaciente; }
    public void setSelectedPaciente(PacienteResponseDto paciente) { this.selectedPaciente = paciente; }

    // --- Dialog Builders ---
    public void showBuscarPacienteDialog() {
        if (buscarPacienteDialog == null) {
            buscarPacienteDialog = buildDialog("Buscar Paciente", buscarPatientPanel);
        }
        if (!buscarPacienteDialog.isVisible()) buscarPacienteDialog.setVisible(true);
        else buscarPacienteDialog.toFront();
    }

    public void showAgregarMedicamentoDialog() {
        if (agregarMedicamentoDialog == null) {
            agregarMedicamentoDialog = buildDialog("Agregar Medicamento", agregarMedicamentoPanel);
        }
        if (!agregarMedicamentoDialog.isVisible()) agregarMedicamentoDialog.setVisible(true);
        else agregarMedicamentoDialog.toFront();
    }

    public void showDetalleDialog() {
        if (detalleDialog == null) {
            detalleDialog = buildDialog("Detalle Medicamento", detallePanel);
        }
        if (!detalleDialog.isVisible()) detalleDialog.setVisible(true);
        else detalleDialog.toFront();
    }

    public void closeBuscarPacienteDialog() {
        if (buscarPacienteDialog != null) buscarPacienteDialog.setVisible(false);
    }

    public void closeAgregarMedicamentoDialog() {
        if (agregarMedicamentoDialog != null) agregarMedicamentoDialog.setVisible(false);
    }

    public void closeDetalleDialog() {
        if (detalleDialog != null) detalleDialog.setVisible(false);
    }

    private JDialog buildDialog(String title, JPanel content) {
        JDialog d = new JDialog(SwingUtilities.getWindowAncestor(mainPanel), title, Dialog.ModalityType.APPLICATION_MODAL);
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(content, BorderLayout.CENTER);
        d.setContentPane(wrapper);
        d.pack();
        d.setLocationRelativeTo(mainPanel);
        d.setResizable(false);
        return d;
    }

    private void initDatePicker() {
        DatePicker = new JDateChooser();
        DatePicker.setDate(new Date());
        datePickerPanel.setLayout(new BorderLayout());
        datePickerPanel.add(DatePicker, BorderLayout.CENTER);
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
        buscarMedicamentoMenuButton = new BlueRoundedButton("Buscar Medicamento");
    }
}