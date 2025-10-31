package org.example.Presentation.Views;

import org.example.Presentation.Components.BlueRoundedButton;
import org.example.Presentation.Components.LoadingOverlay;
import org.example.Presentation.Models.RecetaTableModel;
import org.example.Utilities.EstadoReceta;

import javax.swing.*;
import java.awt.*;

public class DespachoForm extends JPanel {

    private JPanel MainPanel;
    private JPanel searchPanel;
    private JPanel infoPanel;
    private JPanel estadoPanel;
    private JPanel tablePanel;
    private JTextField DatoPacienteField;
    private JButton limpiarButton;
    private JTable recetasTable;
    private JScrollPane scrollPane;
    private JLabel pacienteInfoLabel;
    private JLabel telefonoLabel;
    private JLabel fechaNacimientoLabel;
    private JComboBox<EstadoReceta> estadoComboBox;
    private JButton cambiarEstadoButton;
    private JComboBox<String> searchBycomboBox;
    private JButton buscarButton;

    private final RecetaTableModel recetaTableModel;
    private final LoadingOverlay loadingOverlay;
    private final JFrame parentFrame;

    public DespachoForm(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        this.recetaTableModel = new RecetaTableModel();
        this.loadingOverlay = new LoadingOverlay(parentFrame);

        setLayout(new BorderLayout());
        add(MainPanel, BorderLayout.CENTER);

        initComponents();
    }

    private void initComponents() {
        searchBycomboBox.setModel(new DefaultComboBoxModel<>(new String[]{"ID", "Nombre"}));

        recetasTable.setModel(recetaTableModel);

        estadoComboBox.removeAllItems();
        for (EstadoReceta e : EstadoReceta.values()) {
            estadoComboBox.addItem(e);
        }
    }

    public void showLoading(boolean visible) {
        loadingOverlay.show(visible);
    }

    public void clearFields() {
        DatoPacienteField.setText("");
        pacienteInfoLabel.setText("Seleccione un paciente");
        telefonoLabel.setText("");
        fechaNacimientoLabel.setText("");
    }

    public void setPacienteInfo(String nombre, String telefono, String fechaNacimiento) {
        pacienteInfoLabel.setText("Paciente: " + nombre);
        telefonoLabel.setText("Teléfono: " + telefono);
        fechaNacimientoLabel.setText("Nacimiento: " + fechaNacimiento);
    }

    // Getters
    public JPanel getMainPanel() { return MainPanel; }
    public RecetaTableModel getTableModel() { return recetaTableModel; }
    public JTable getRecetasTable() { return recetasTable; }
    public JTextField getDatoPacienteField() { return DatoPacienteField; }
    public JComboBox<String> getSearchByComboBox() { return searchBycomboBox; }
    public JComboBox<EstadoReceta> getEstadoComboBox() { return estadoComboBox; }
    public JButton getBuscarButton() { return buscarButton; }
    public JButton getLimpiarButton() { return limpiarButton; }
    public JButton getCambiarEstadoButton() { return cambiarEstadoButton; }

    private void createUIComponents() {
        buscarButton = new BlueRoundedButton("Buscar");
        limpiarButton = new BlueRoundedButton( "Limpiar") ;
        cambiarEstadoButton = new BlueRoundedButton( "Cambiar Estado") ;
    }
}
