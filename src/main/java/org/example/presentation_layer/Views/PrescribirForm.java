package org.example.presentation_layer.Views;

import com.toedter.calendar.JDateChooser;
import org.example.presentation_layer.Components.BlueRoundedButton;
import org.example.presentation_layer.Components.CustomTextField;
import org.example.presentation_layer.Models.MedicamentoTableModel;
import org.example.presentation_layer.Models.PacienteTableModel;
import org.example.service_layer.MedicamentoService;
import org.example.service_layer.PacienteService;
import org.example.service_layer.UsuarioService;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

public class PrescribirForm {
    private JPanel mainPanel;
    private JPanel controlPanel;
    private JButton buscarPacienteButton;
    private JButton agregarMedicamentoButton;
    private JPanel RectaPanel;
    private JTable MedicamentoRecetatable;
    private JButton guardarButton;
    private JButton detallesButton;
    private JButton limpiarButton;
    private JButton descartarButton;
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
    private JButton guardarButton1;
    private JButton cancerlarButton;
    private JPanel DetallePanel;
    private JButton cancelButton;
    private JButton guardarPacienteButton;
    private JButton cancelMedicamentoButton;
    private JButton guardarMedicamentoButton;
    private JPanel window;
    private JPanel DatePickerPanel;

    private JDateChooser DatePicker;

    public PrescribirForm(UsuarioService usuarioService, PacienteService pacienteService, MedicamentoService medicamentoService, int idMedico) {


        initDatePickers();
        MedicamentosTable.setModel(new MedicamentoTableModel(medicamentoService.leerTodos()));
        pacientesTable.setModel(new PacienteTableModel(pacienteService.leerTodos()));

    }

    private void initDatePickers() {
        DatePickerPanel.setSize(300, 30);
        DatePickerPanel.setLayout(new BorderLayout());

        DatePicker = new JDateChooser();
        DatePicker.setSize(300, 30);
        DatePicker.setDate(new Date());
        DatePickerPanel.add(DatePicker, BorderLayout.CENTER);

        DatePickerPanel.revalidate();
        DatePickerPanel.repaint();
    }

    public  JPanel getMainPanel() {
        return mainPanel;
    }

    private void createUIComponents() {
        //all buttons are blue rounded buttons
        buscarPacienteButton = new BlueRoundedButton( "Buscar Paciente") ;
        agregarMedicamentoButton = new BlueRoundedButton( "Agregar Medicamento") ;
        guardarButton = new BlueRoundedButton( "Guardar") ;
        detallesButton = new BlueRoundedButton( "Detalles") ;
        limpiarButton = new BlueRoundedButton( "Limpiar") ;
        descartarButton = new BlueRoundedButton( "Descartar") ;
        guardarButton1 = new BlueRoundedButton( "Guardar") ;
        cancerlarButton = new BlueRoundedButton( "Cancelar") ;
        cancelButton = new BlueRoundedButton( "Cancelar") ;
        guardarPacienteButton = new BlueRoundedButton( "Guardar") ;
        cancelMedicamentoButton = new BlueRoundedButton( "Cancelar") ;
        guardarMedicamentoButton = new BlueRoundedButton( "Guardar") ;
        //all text fields are rounded
        busquedaPacientetextField = new CustomTextField() ;
        BusquedaMedicamentoTextField = new CustomTextField() ;




    }
}
