package org.example.presentation_layer.Controllers;

import org.example.domain_layer.Medico;
import org.example.domain_layer.Paciente;
import org.example.presentation_layer.Models.MedicosTableModel;
import org.example.presentation_layer.Models.PacienteTableModel;
import org.example.presentation_layer.Views.PacienteForm;
import org.example.service_layer.IService;
import org.example.service_layer.PacienteService;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PacienteController {
    private final PacienteForm view;
    private final PacienteService pacienteService;
    private final PacienteTableModel tableModel;

    public PacienteController(PacienteForm view, PacienteService pacienteService) {
        this.view = view;
        this.pacienteService = pacienteService;
        this.tableModel = new PacienteTableModel(new ArrayList<>());
        this.view.getPacientestable().setModel(tableModel);
        cargarPacientes();
    }


    public void guardarPaciente() {
        try {
            int id = Integer.parseInt(view.getIDtextFiel().getText().trim());
            String nombre = view.getNametextField().getText().trim();
            String telefono = view.getTelefonotextField().getText().trim();
            //Date fechaNacimiento = view.getFechaNacimientodateChooser().getDate();

            Paciente p = new Paciente(id, nombre, telefono, new Date() /*fechaNacimiento*/);

           pacienteService.agregar(p);

           cargarPacientes();
           limpiarCampos();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(view, "ID inválido", "Error", JOptionPane.ERROR_MESSAGE);

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(view, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void actualizarPaciente() {
   // Implementar la lógica para actualizar un paciente ya existente
        try {
            int id = Integer.parseInt(view.getIDtextFiel().getText().trim());
            String nombre = view.getNametextField().getText().trim();
            String telefono = view.getTelefonotextField().getText().trim();
            //Date fechaNacimiento = view.getFechaNacimientodateChooser().getDate();

            Paciente p = new Paciente(id, nombre, telefono, new Date() /*fechaNacimiento*/);

            pacienteService.actualizar(p);

            cargarPacientes();
            limpiarCampos();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(view, "ID inválido", "Error", JOptionPane.ERROR_MESSAGE);

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(view, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }


    }

    public void borrarPaciente() {
        int row = view.getPacientestable().getSelectedRow();
        if (row >= 0) {
            int id = (int) tableModel.getValueAt(row, 0);
            pacienteService.borrar(id);
            cargarPacientes();
        }

    }

    public  void buscarPaciente() {
        String filtro = view.getBuscartextField().getText().toLowerCase().trim();
        if (filtro.isEmpty()) {
            cargarPacientes();
            return;
        }

        List<Paciente> filtrados = new ArrayList<>();
        for (Paciente p : pacienteService.leerTodos()) {
            if (p.getNombre() != null && p.getNombre().toLowerCase().contains(filtro)) {
                filtrados.add(p);
            }
        }
        tableModel.setPacientes(filtrados);
    }

    public void limpiarCampos() {
        view.getIDtextFiel().setText("");
        view.getNametextField().setText("");
        view.getTelefonotextField().setText("");
        //view.getFechaNacimientodateChooser().setDate(null);
    }


    private void cargarPacientes() {
        List<Paciente> pacientes = new ArrayList<>();
        for (Paciente p : pacienteService.leerTodos()) {
            pacientes.add(p);
        }
        tableModel.setPacientes(pacientes);
    }

}

