package org.example.presentation_layer.Views;

import org.example.Domain.Dtos.users.Farmaceuta;
import org.example.presentation_layer.Components.BlueRoundedButton;
import org.example.presentation_layer.Controllers.FarmaceutaController;
import org.example.presentation_layer.Models.FarmaceutaTableModel;
import org.example.service_layer.UsuarioService;

import javax.swing.*;
import java.util.List;

public class FarmaceutaForm extends JPanel {
    private JPanel MainPanel;
    private JPanel BanerPanel;
    private JPanel FarmaceutaPanel;
    private JPanel TableFarmaceuta;
    private JTable Farmaceutastable;
    private JPanel BusquedaFarmaceuta;
    private JTextField BuscartextField;
    private JButton buscarButton;
    private JButton reporteButton;
    private JPanel ManagerFarmaceuta;
    private JTextField IDtextFiel;
    private JTextField NametextField;
    private JButton guardarButton;
    private JButton limpiarButton;
    private JButton borrarButton;
    private JButton actualizarButton;

    private FarmaceutaTableModel farmaceutaModel;
    private final FarmaceutaController farmaceutaController;

    public FarmaceutaForm(UsuarioService usuarioService) {
        List<Farmaceuta> farmaceutas = usuarioService.leerPorTipo(Farmaceuta.class)
                .stream()
                .filter(Farmaceuta.class::isInstance)
                .map(Farmaceuta.class::cast)
                .toList();
        this.farmaceutaModel = new FarmaceutaTableModel(farmaceutas);
        this.farmaceutaController = new FarmaceutaController(this, usuarioService, farmaceutaModel);

        Farmaceutastable.setModel(farmaceutaModel);

        buscarButton.addActionListener(e -> farmaceutaController.buscarFarmaceuta());
        guardarButton.addActionListener(e -> farmaceutaController.guardarFarmaceuta());
        limpiarButton.addActionListener(e -> farmaceutaController.limpiarCampos());
        borrarButton.addActionListener(e -> farmaceutaController.borrarFarmaceuta());
        actualizarButton.addActionListener(e -> farmaceutaController.actualizarFarmaceuta());
        reporteButton.addActionListener(e -> farmaceutaController.generarReporteFarmaceutaSeleccionado());
        Farmaceutastable.getSelectionModel().addListSelectionListener(this::onTableSelection);
    }



    private void onTableSelection(javax.swing.event.ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) return;
        if (farmaceutaModel == null) return;
        int row = Farmaceutastable.getSelectedRow();
        if (row < 0) return;
        Farmaceuta f = farmaceutaModel.getFarmaceutaAt(row);
        if (f == null) return;

        IDtextFiel.setText(String.valueOf(f.getId()));
        NametextField.setText(f.getNombre());
    }


    //gets
    public JPanel getMainPanel() { return MainPanel; }
    public JTextField getBuscartextField() { return BuscartextField; }
    public JTextField getIDtextFiel() { return IDtextFiel; }
    public JTextField getNametextField() { return NametextField; }
    public JTable getFarmaceutatable() { return Farmaceutastable; }
    public JButton getBuscarButton() { return buscarButton; }
    public JButton getReporteButton() { return reporteButton; }
    public JButton getGuardarButton() { return guardarButton; }
    public JButton getLimpiarButton() { return limpiarButton; }
    public JButton getBorrarButton() { return borrarButton; }



    private void createUIComponents() {
        buscarButton = new BlueRoundedButton("Buscar");
        reporteButton = new BlueRoundedButton("Reporte");
        guardarButton = new BlueRoundedButton("Guardar");
        limpiarButton = new BlueRoundedButton("Limpiar");
        borrarButton = new BlueRoundedButton("Borrar");
        actualizarButton = new BlueRoundedButton("Actualizar");
    }
}


