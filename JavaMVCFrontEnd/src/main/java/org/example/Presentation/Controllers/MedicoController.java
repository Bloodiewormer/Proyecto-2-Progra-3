package org.example.Presentation.Controllers;
import org.example.presentation_layer.Models.MedicoTableModel;
import org.example.presentation_layer.Views.MedicoForm;
import org.example.Services.UsuarioService;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class MedicoController {
    private final MedicoForm view;
    private final MedicoTableModel tableModel;
    private final UsuarioService usuarioService;

    public MedicoController(MedicoForm view, UsuarioService usuarioService, MedicoTableModel tableModel) {
        this.view = view;
        this.usuarioService = usuarioService;
        this.tableModel = tableModel;
        cargarMedicos();
    }

    public void guardarMedico() {
        try {
            int id = Integer.parseInt(view.getIDtextField().getText().trim());
            validarId(id); // Validate before adding
            String nombre = view.getNametextField().getText().trim();
            String especialidad = view.getEspecialidadtextField().getText().trim();
            Medico m = new Medico(id, "", nombre, especialidad);
            usuarioService.agregar(m);
            cargarMedicos();
            limpiarCampos();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(view, "ID inválido", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(view, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void limpiarCampos() {
        view.getIDtextField().setText("");
        view.getEspecialidadtextField().setText("");
        view.getNametextField().setText("");
    }

    public void actualizarMedico() {
        try {
            int id = Integer.parseInt(view.getIDtextField().getText().trim());
            String nombre = view.getNametextField().getText().trim();
            String especialidad = view.getEspecialidadtextField().getText().trim();
            Usuario existing = usuarioService.leerPorId(id);
            if (!(existing instanceof Medico)) {
                throw new IllegalArgumentException("Medico not found.");
            }
            String password = existing.getPassword();
            Medico m = new Medico(id,password, nombre, especialidad);
            usuarioService.actualizar(m);
            cargarMedicos();
            limpiarCampos();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(view, "ID inválido", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(view, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
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

    private void validarId(int id) {
        for (Usuario u : usuarioService.leerTodos()) {
            if (u.getId() == id) {
                throw new IllegalArgumentException("ID ya existe. Por favor, ingrese un ID único.");
            }
        }
    }

    public void generarReporteMedicoSeleccionado() {
        int row = view.getMedicostable().getSelectedRow();
        if (row >= 0) {
            int id = (int) tableModel.getValueAt(row, 0);
            Usuario u = usuarioService.leerPorId(id);
            if (u != null) {
                String reporte = "Reporte de Usuario\n\n" +
                        "ID: " + u.getId() + "\n" +
                        "Nombre: " + u.getNombre() + "\n" +
                        "Tipo: " + u.getClass().getSimpleName() + "\n";

                JOptionPane.showMessageDialog(view, reporte, "Reporte de Usuario", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(view, "No se encontró el usuario seleccionado.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(view, "Por favor, seleccione un usuario de la tabla.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }


}
