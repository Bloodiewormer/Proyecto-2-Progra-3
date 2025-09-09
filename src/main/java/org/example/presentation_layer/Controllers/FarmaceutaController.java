package org.example.presentation_layer.Controllers;

import org.example.domain_layer.Farmaceuta;
import org.example.domain_layer.Usuario;
import org.example.presentation_layer.Models.FarmaceutaTableModel;
import org.example.presentation_layer.Views.FarmaceutaForm;
import org.example.service_layer.UsuarioService;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class FarmaceutaController {
    private final FarmaceutaForm view;
    private final FarmaceutaTableModel tableModel;
    private final UsuarioService usuarioService;

    public FarmaceutaController(FarmaceutaForm view, UsuarioService usuarioService) {
        this.view = view;
        this.usuarioService = usuarioService;
        this.tableModel = new FarmaceutaTableModel(new ArrayList<>());
        this.view.getFarmaceutatable().setModel(tableModel);
        cargarFarmaceuta();
    }

    public void guardarFarmaceuta() {
        try {
            int id = Integer.parseInt(view.getIDtextFiel().getText().trim());
            String nombre = view.getNametextField().getText().trim();


            Farmaceuta m = new Farmaceuta(id, "", nombre);
            usuarioService.agregar(m);

            cargarFarmaceuta();
            limpiarCampos();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(view, "ID inválido");
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(view, ex.getMessage());
        }
    }

    public void limpiarCampos() {
        view.getIDtextFiel().setText("");
        view.getNametextField().setText("");
    }

    public void borrarMedico() {
        int row = view.getFarmaceutatable().getSelectedRow();
        if (row >= 0) {
            int id = (int) tableModel.getValueAt(row, 0);
            usuarioService.borrar(id);
            cargarFarmaceuta();
        }
    }

    public void buscarFarmaceuta() {
        String filtro = view.getBuscartextField().getText().toLowerCase().trim();
        List<Farmaceuta> filtrados = new ArrayList<>();
        for (Usuario u : usuarioService.leerTodos()) {
            if (u instanceof Farmaceuta) {
                Farmaceuta m = (Farmaceuta) u;
                if (m.getNombre() != null && m.getNombre().toLowerCase().contains(filtro)) {
                    filtrados.add(m);
                }
            }
        }
        tableModel.setFarmaceuta(filtrados);
    }

    private void cargarFarmaceuta() {
        List<Farmaceuta> farmaceutas = new ArrayList<>();
        for (Usuario u : usuarioService.leerTodos()) {
            if (u instanceof Farmaceuta) {
                farmaceutas.add((Farmaceuta) u);
            }
        }
        tableModel.setFarmaceuta(farmaceutas);
    }
}
