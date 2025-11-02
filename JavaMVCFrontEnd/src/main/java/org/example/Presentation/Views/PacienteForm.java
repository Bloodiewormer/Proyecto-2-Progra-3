package org.example.Presentation.Views;

import com.toedter.calendar.JDateChooser;
import org.example.Domain.Dtos.Paciente.PacienteResponseDto;
import org.example.Presentation.Components.BlueRoundedButton;
import org.example.Presentation.Components.LoadingOverlay;
import org.example.Presentation.Models.PacienteTableModel;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PacienteForm extends JPanel {
    private JPanel mainPanel;
    private JPanel bannerPanel;
    private JPanel patientPanel;
    private JPanel tablePacientesPanel;
    private JPanel busquedaPacientePanel;
    private JPanel managerPacientePanel;
    private JPanel datePickerPanel;
    private JTextField phoneTextField;
    private JTextField buscarTextField;
    private JTextField idTextField;
    private JTextField NameTextField;
    private JButton guardarButton;
    private JButton limpiarButton;
    private JButton borrarButton;
    private JButton buscarButton;
    private JButton reporteButton;
    private JButton actualizarButton;
    private JTable patientsTable;

    private JDateChooser datePicker;
    private final PacienteTableModel tableModel;
    private final LoadingOverlay loadingOverlay;
    private final JFrame parentFrame;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public PacienteForm(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        this.tableModel = new PacienteTableModel();
        this.loadingOverlay = new LoadingOverlay(parentFrame);

        initDatePickers();
        patientsTable.setModel(tableModel);
        setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);
    }

    private void initDatePickers() {
        datePickerPanel.setLayout(new BorderLayout());
        datePicker = new JDateChooser();
        datePicker.setDate(new Date());
        datePickerPanel.add(datePicker, BorderLayout.CENTER);
        datePickerPanel.revalidate();
        datePickerPanel.repaint();
    }

    public void showLoading(boolean visible) {
        loadingOverlay.show(visible);
    }

    public void clearFields() {
        idTextField.setText("");
        NameTextField.setText("");
        phoneTextField.setText("");
        datePicker.setDate(new Date());
        patientsTable.clearSelection();
    }

    public void populateFields(PacienteResponseDto paciente) {
        idTextField.setText(String.valueOf(paciente.getId()));
        NameTextField.setText(paciente.getNombre());
        phoneTextField.setText(paciente.getTelefono());

        try {
            if (paciente.getFechaNacimiento() != null) {
                Date fecha = dateFormat.parse(paciente.getFechaNacimiento());
                datePicker.setDate(fecha);
            }
        } catch (Exception ex) {
            // Ignore parse errors
        }
    }

    // Getters
    public PacienteTableModel getTableModel() { return tableModel; }
    public JTable getPatientsTable() { return patientsTable; }
    public JTextField getBuscarTextField() { return buscarTextField; }
    public JTextField getIdTextField() { return idTextField; }
    public JTextField getNameTextField() { return NameTextField; }
    public JTextField getTelefonoTextField() { return phoneTextField; }
    public JDateChooser getDatePicker() { return datePicker; }
    public JButton getBuscarButton() { return buscarButton; }
    public JButton getGuardarButton() { return guardarButton; }
    public JButton getActualizarButton() { return actualizarButton; }
    public JButton getBorrarButton() { return borrarButton; }
    public JButton getLimpiarButton() { return limpiarButton; }
    public JButton getReporteButton() { return reporteButton; }
    public JPanel getMainPanel() { return mainPanel; }

    private void createUIComponents() {
        buscarButton = new BlueRoundedButton("Buscar");
        reporteButton = new BlueRoundedButton("Reporte");
        guardarButton = new BlueRoundedButton("Guardar");
        limpiarButton = new BlueRoundedButton("Limpiar");
        borrarButton = new BlueRoundedButton("Borrar");
        actualizarButton = new BlueRoundedButton("Actualizar");
    }
}