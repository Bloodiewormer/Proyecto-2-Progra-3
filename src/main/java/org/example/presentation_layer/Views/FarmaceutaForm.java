package org.example.presentation_layer.Views;

import org.example.presentation_layer.Components.BlueRoundedButton;
import org.example.presentation_layer.Controllers.FarmaceutaController;
import org.example.service_layer.UsuarioService;

import javax.swing.*;
import java.awt.*;

public class FarmaceutaForm extends Component {
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

    public JPanel getMainPanel() {
        return MainPanel;
    }

    private final FarmaceutaController farmaceutaController;

    public FarmaceutaForm(UsuarioService usuarioService) {
        farmaceutaController = new FarmaceutaController(this, usuarioService);

        buscarButton.addActionListener(e -> farmaceutaController.buscarFarmaceuta());
        guardarButton.addActionListener(e -> farmaceutaController.guardarFarmaceuta());
        limpiarButton.addActionListener(e -> farmaceutaController.limpiarCampos());
        borrarButton.addActionListener(e -> farmaceutaController.borrarMedico());
    }

    //gets
    public JTextField getBuscartextField() {return BuscartextField;}
    public JTextField getIDtextFiel() {return IDtextFiel;}
    public JTextField getNametextField() {return NametextField;}
    public JTable getFarmaceutatable() {return Farmaceutastable;}



    private void createUIComponents() {
        buscarButton = new BlueRoundedButton("Buscar");
        reporteButton = new BlueRoundedButton("Reporte");
        guardarButton = new BlueRoundedButton("Guardar");
        limpiarButton = new BlueRoundedButton("Limpiar");
        borrarButton = new BlueRoundedButton("Borrar");
    }

}
