package org.example.presentation_layer.Views;

import com.toedter.calendar.JDateChooser;
import org.example.presentation_layer.Components.BlueRoundedButton;
import org.example.presentation_layer.Controllers.PacienteController;
import org.example.presentation_layer.Models.PacienteTableModel;
import org.example.service_layer.PacienteService;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.util.Date;

public class PacienteForm extends JPanel {
    private JPanel mainPanel;
    @SuppressWarnings( "unused" )
    private JPanel bannerPanel;
    @SuppressWarnings( "unused" )
    private JPanel patientPanel;
    @SuppressWarnings( "unused" )
    private JPanel tablePacientesPanel;
    @SuppressWarnings( "unused" )
    private JPanel busquedaPacientePanel;
    @SuppressWarnings( "unused" )
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
    private final PacienteTableModel pacienteModel;
    private final PacienteController pacienteController;


    public PacienteForm(PacienteService patientService) {
        initDatePickers();
        initListeners();

        this.pacienteModel = new PacienteTableModel(patientService.leerTodos());
        this.pacienteController = new PacienteController(this, patientService, pacienteModel);

        patientsTable.setModel(pacienteModel);
    }

    private void initListeners() {
        buscarButton.addActionListener(_ -> pacienteController.buscarPaciente());
        guardarButton.addActionListener(_ -> pacienteController.guardarPaciente());
        limpiarButton.addActionListener(_ -> pacienteController.limpiarCampos());
        borrarButton.addActionListener(_ -> pacienteController.borrarPaciente());
        actualizarButton.addActionListener(_ -> pacienteController.actualizarPaciente());
        reporteButton.addActionListener(_ -> pacienteController.generarReportePacienteSeleccionado() );
        patientsTable.getSelectionModel().addListSelectionListener(this::onTableSelection);
    }


    //Selectores de fecha
    private void initDatePickers() {
        datePickerPanel.setLayout(new BorderLayout());

        datePicker = new JDateChooser();
        datePicker.setDate(new Date());
        datePickerPanel.add(datePicker, BorderLayout.CENTER);

        datePickerPanel.revalidate();
        datePickerPanel.repaint();
    }

    private void onTableSelection(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) return;
        if (pacienteModel == null) return;
        int row = patientsTable.getSelectedRow();
        if (row < 0) return;
        Paciente p = pacienteModel.getPacienteAt(row);
        if (p == null) return;

        idTextField.setText(String.valueOf(p.getId()));
        NameTextField.setText(p.getNombre());
        phoneTextField.setText(p.getTelefono());
        datePicker.setDate(p.getFechaNacimiento());
    }

    public JTable getPatientsTable() { return patientsTable; }
    public JTextField getBuscarTextField() { return buscarTextField; }
    public JTextField getIdTextField() { return idTextField; }
    public JTextField getNameTextField() { return NameTextField; }
    public JTextField getTelefonoTextField() { return phoneTextField; }
    public JDateChooser getDatePicker() { return datePicker; }
    public JButton getBuscarButton() { return buscarButton; }
    public JPanel getMainPanel() { return mainPanel; }

    private void createUIComponents() {
        buscarButton = new BlueRoundedButton( "Buscar");
        reporteButton = new BlueRoundedButton( "Reporte");
        guardarButton = new BlueRoundedButton( "Guardar");
        limpiarButton = new BlueRoundedButton( "Limpiar");
        borrarButton = new BlueRoundedButton( "Borrar");
        actualizarButton = new BlueRoundedButton( "Actualizar");

    }
}