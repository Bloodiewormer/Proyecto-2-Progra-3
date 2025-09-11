package org.example.presentation_layer.Views;

import org.example.presentation_layer.Components.BlueRoundedButton;
import org.example.presentation_layer.Controllers.MedicamentoController;
import org.example.service_layer.MedicamentoService;

import javax.swing.*;
import java.awt.*;

public class MedicamentoForm extends JPanel {
    private JPanel MainPanel;
    private JPanel BanerPanel;
    private JPanel MedicamentoPanel;
    private JPanel TableMedicamentos;
    private JTable Medicamentotable;
    private JPanel BusquedaMedicamentos;
    private JTextField BuscartextField;
    private JButton buscarButton;
    private JButton reporteButton;
    private JPanel ManagerMedicamento;
    private JTextField CodigotextField;
    private JTextField NametextField;
    private JButton guardarButton;
    private JButton limpiarButton;
    private JButton borrarButton;
    private JLabel Codigolable;
    private JLabel NameLable;
    private JTextField PresentaciontextField;

    private MedicamentoController medicamentoController;

    public MedicamentoForm(MedicamentoService medicamentoService) {
        this.medicamentoController = new MedicamentoController(this, medicamentoService);


        buscarButton.addActionListener(e -> medicamentoController.buscarMedicamento());
        guardarButton.addActionListener(e -> medicamentoController.agregarMedicamento());
        limpiarButton.addActionListener(e -> medicamentoController.limpiarCampos());
        borrarButton.addActionListener(e -> medicamentoController.borrarMedicamento());
    }

    public JPanel getMainPanel() {
        return MainPanel;
    }

    public JTable getMedicamentostable() {return Medicamentotable;}
    public JTextField getBuscartextField() {return BuscartextField;}
    public JTextField getCodigotextField() {return CodigotextField;}
    public JTextField getNametextField() {return NametextField;}
    public JTextField getPresentaciontextField() {return PresentaciontextField;}



    private void createUIComponents() {

        buscarButton = new BlueRoundedButton( "Buscar");
        reporteButton = new BlueRoundedButton( "Reporte");
        guardarButton = new BlueRoundedButton( "Guardar");
        limpiarButton = new BlueRoundedButton( "Limpiar");
        borrarButton = new BlueRoundedButton( "Borrar");

    }
}
