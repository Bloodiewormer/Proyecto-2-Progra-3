package org.example.presentation_layer.Views;

import org.example.Domain.Dtos.users.Medico;
import org.example.presentation_layer.Components.BlueRoundedButton;
import org.example.presentation_layer.Controllers.MedicoController;
import org.example.presentation_layer.Models.MedicoTableModel;
import org.example.service_layer.UsuarioService;

import javax.swing.*;
import java.util.List;

public class MedicoForm extends JPanel {

    private JPanel MainPanel;
    private JPanel BanerPanel;
    private JPanel MedicoPanel;
    private JPanel ManagerMedicos;
    private JPanel BusquedaMedicos;
    private JPanel TableMedicos;
    private JTable Medicostable;
    private JTextField BuscartextField;
    private JButton buscarButton;
    private JButton reporteButton;
    private JTextField idTextField;
    private JTextField EspecialidadtextField;
    private JTextField NametextField;
    private JButton guardarButton;
    private JButton limpiarButton;
    private JButton borrarButton;
    private JButton actualizarButton;

    private MedicoController medicoController;
    private MedicoTableModel medicoModel;

    public MedicoForm(UsuarioService usuarioService) {
        List<Medico> medicos = usuarioService.leerPorTipo(Medico.class)
                .stream()
                .filter(Medico.class::isInstance)
                .map(Medico.class::cast)
                .toList();
        this.medicoModel = new MedicoTableModel(medicos);
        this.medicoController = new MedicoController(this, usuarioService, medicoModel);

        Medicostable.setModel(medicoModel);

        buscarButton.addActionListener(e -> medicoController.buscarMedico());
        guardarButton.addActionListener(e -> medicoController.guardarMedico());
        limpiarButton.addActionListener(e -> medicoController.limpiarCampos());
        borrarButton.addActionListener(e -> medicoController.borrarMedico());
        actualizarButton.addActionListener(e -> medicoController.actualizarMedico());
        reporteButton.addActionListener(e -> medicoController.generarReporteMedicoSeleccionado());
        Medicostable.getSelectionModel().addListSelectionListener(this::onTableSelection);
    }


    public JPanel getMainPanel() { return MainPanel; }
    public JTextField getBuscartextField() { return BuscartextField; }
    public JTextField getIDtextField() { return idTextField; }
    public JTextField getEspecialidadtextField() { return EspecialidadtextField; }
    public JTextField getNametextField() { return NametextField; }
    public JTable getMedicostable() { return Medicostable; }
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
        if (medicoModel == null) return;
        int row = Medicostable.getSelectedRow();
        if (row < 0) return;
        Medico m = medicoModel.getMedicoAt(row);
        if (m == null) return;

        idTextField.setText(String.valueOf(m.getId()));
        NametextField.setText(m.getNombre());
        EspecialidadtextField.setText(m.getEspecialidad());
    }

}
