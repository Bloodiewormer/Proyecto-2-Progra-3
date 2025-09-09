package org.example.presentation_layer.Controllers;

import org.example.domain_layer.Medico;
import org.example.domain_layer.Usuario;
import org.example.presentation_layer.Models.MedicosTableModel;
import org.example.presentation_layer.Views.MedicoForm;
import org.example.service_layer.UsuarioService;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class MedicoController {
    private final MedicoForm view;
    private final MedicosTableModel tableModel;
    private final UsuarioService usuarioService;

    public MedicoController(MedicoForm view, UsuarioService usuarioService) {
        this.view = view;
        this.usuarioService = usuarioService;
        this.tableModel = new MedicosTableModel(new ArrayList<>());
        this.view.getMedicostable().setModel(tableModel);
        cargarMedicos();
    }

    public void guardarMedico() {
        try {
            int id = Integer.parseInt(view.getIDtextFiel().getText().trim());
            String nombre = view.getNametextField().getText().trim();
            String especialidad = view.getEspecialidadtextField().getText().trim();

            Medico m = new Medico(id, "", nombre, especialidad);
            usuarioService.agregar(m);

            cargarMedicos();
            limpiarCampos();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(view, "ID inválido");
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(view, ex.getMessage());
        }
    }

    public void limpiarCampos() {
        view.getIDtextFiel().setText("");
        view.getEspecialidadtextField().setText("");
        view.getNametextField().setText("");
    }

    public void borrarMedico() {
        int row = view.getMedicostable().getSelectedRow();
        if (row >= 0) {
            int id = (int) tableModel.getValueAt(row, 0);
            usuarioService.borrar(id);
            cargarMedicos();
        }
    }

    public void buscarMedico() {
        String filtro = view.getBuscartextField().getText().toLowerCase().trim();
        List<Medico> filtrados = new ArrayList<>();
        for (Usuario u : usuarioService.leerTodos()) {
            if (u instanceof Medico) {
                Medico m = (Medico) u;
                if (m.getNombre() != null && m.getNombre().toLowerCase().contains(filtro)) {
                    filtrados.add(m);
                }
            }
        }
        tableModel.setMedicos(filtrados);
    }

    private void cargarMedicos() {
        List<Medico> medicos = new ArrayList<>();
        for (Usuario u : usuarioService.leerTodos()) {
            if (u instanceof Medico) {
                medicos.add((Medico) u);
            }
        }
        tableModel.setMedicos(medicos);
    }
}
