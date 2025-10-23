package org.example.presentation_layer.Views;

import org.example.Domain.Dtos.medicamento.Medicamento;
import org.example.presentation_layer.Components.BlueRoundedButton;
import org.example.presentation_layer.Controllers.MedicamentoController;
import org.example.presentation_layer.Models.MedicamentoTableModel;
import org.example.service_layer.MedicamentoService;

import javax.swing.*;
import java.util.List;

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
    private JButton actualizarButton;

    private MedicamentoTableModel medicamentoModel;
    private MedicamentoController medicamentoController;


    public MedicamentoForm(MedicamentoService medicamentoService) {
        List<Medicamento> medicamentos = medicamentoService.leerTodos();
        this.medicamentoModel = new MedicamentoTableModel(medicamentos);
        this.medicamentoController = new MedicamentoController(this, medicamentoService, medicamentoModel);

        Medicamentotable.setModel(medicamentoModel);

        buscarButton.addActionListener(e -> medicamentoController.buscarMedicamento());
        guardarButton.addActionListener(e -> medicamentoController.agregarMedicamento());
        limpiarButton.addActionListener(e -> medicamentoController.limpiarCampos());
        borrarButton.addActionListener(e -> medicamentoController.borrarMedicamento());
        actualizarButton.addActionListener(e -> medicamentoController.actualizarMedicamento());
        reporteButton.addActionListener(e -> medicamentoController.generarReporteMedicamentoSeleccionado());
        Medicamentotable.getSelectionModel().addListSelectionListener(this::onTableSelection);
    }

    public JPanel getMainPanel() { return MainPanel; }
    public JTable getMedicamentotable() { return Medicamentotable; }
    public JTextField getBuscartextField() { return BuscartextField; }
    public JTextField getCodigotextField() { return CodigotextField; }
    public JTextField getNametextField() { return NametextField; }
    public JTextField getPresentaciontextField() { return PresentaciontextField; }
    public JButton getBuscarButton() { return buscarButton; }
    public JButton getReporteButton() { return reporteButton; }
    public JButton getGuardarButton() { return guardarButton; }
    public JButton getLimpiarButton() { return limpiarButton; }
    public JButton getBorrarButton() { return borrarButton; }
    public JButton getActualizarButton() { return actualizarButton; }




    private void createUIComponents() {
        buscarButton = new BlueRoundedButton("Buscar");
        reporteButton = new BlueRoundedButton("Reporte");
        guardarButton = new BlueRoundedButton("Guardar");
        limpiarButton = new BlueRoundedButton("Limpiar");
        borrarButton = new BlueRoundedButton("Borrar");
        actualizarButton = new BlueRoundedButton("Actualizar");
    }

    private void onTableSelection(javax.swing.event.ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) return;
        if (medicamentoModel == null) return;
        int row = Medicamentotable.getSelectedRow();
        if (row < 0) return;
        Medicamento m = medicamentoModel.getMedicamentoAt(row);
        if (m == null) return;

        CodigotextField.setText(String.valueOf(m.getCodigo()));
        NametextField.setText(m.getNombre());
        PresentaciontextField.setText(m.getPresentacion());
    }


}
