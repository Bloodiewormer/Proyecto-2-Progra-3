package org.example.Presentation.Controllers;

import org.example.Presentation.Models.FarmaceutaTableModel;
import org.example.Presentation.Views.FarmaceutaForm;
import org.example.Services.UsuarioService;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class FarmaceutaController {
    private final FarmaceutaForm view;
    private final FarmaceutaTableModel tableModel;
    private final UsuarioService usuarioService;

    public FarmaceutaController(FarmaceutaForm view, UsuarioService usuarioService, FarmaceutaTableModel tableModel) {
        this.view = view;
        this.usuarioService = usuarioService;
        this.tableModel = tableModel;
        cargarFarmaceuta();
    }

    public void guardarFarmaceuta() {
        try {
            int id = Integer.parseInt(view.getIDtextFiel().getText().trim());
            validarId(id);
            String nombre = view.getNametextField().getText().trim();

            Farmaceuta m = new Farmaceuta(id, "", nombre);
            usuarioService.agregar(m);

            cargarFarmaceuta();
            limpiarCampos();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(view, "ID inválido", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(view, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void actualizarFarmaceuta() {
        try {
            int id = Integer.parseInt(view.getIDtextFiel().getText().trim());
            String nombre = view.getNametextField().getText().trim();
            Usuario existing = usuarioService.leerPorId(id);
            if (!(existing instanceof Farmaceuta)) {
                throw new IllegalArgumentException("Farmaceuta not found.");
            }
            String password = existing.getPassword();
            Farmaceuta m = new Farmaceuta(id, password, nombre);
            usuarioService.actualizar(m);
            cargarFarmaceuta();
            limpiarCampos();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(view, "ID inválido", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(view, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    public void limpiarCampos() {
        view.getIDtextFiel().setText("");
        view.getNametextField().setText("");
    }

    public void borrarFarmaceuta() {
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

    private void validarId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID debe ser positivo");
        }
        for (Usuario u : usuarioService.leerTodos()) {
            if (u instanceof Farmaceuta && u.getId() == id) {
                throw new IllegalArgumentException("ID ya existe");
            }
        }
    }

    public void generarReporteFarmaceutaSeleccionado() {
        int row = view.getFarmaceutatable().getSelectedRow();
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
