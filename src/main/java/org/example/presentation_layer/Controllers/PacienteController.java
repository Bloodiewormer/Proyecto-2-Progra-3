package org.example.presentation_layer.Controllers;

import org.example.domain_layer.Paciente;
import org.example.presentation_layer.Models.PacienteTableModel;
import org.example.presentation_layer.Views.PacienteForm;
import org.example.service_layer.PacienteService;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PacienteController {
    private final PacienteForm view;
    private final PacienteService pacienteService;
    private final PacienteTableModel tableModel;

    public PacienteController(PacienteForm view, PacienteService pacienteService, PacienteTableModel tableModel) {
        this.view = view;
        this.pacienteService = pacienteService;
        this.tableModel  = tableModel;
        cargarPacientes();
    }


    public void guardarPaciente() {
        try {
            int id = Integer.parseInt(view.getIDtextFiel().getText().trim());
            String nombre = view.getNametextField().getText().trim();
            String telefono = view.getTelefonotextField().getText().trim();
            Date fechaNacimiento = view.getDatePicker().getDate();

            Paciente p = new Paciente(id, nombre, telefono, fechaNacimiento);

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
            Date fechaNacimiento = view.getDatePicker().getDate();

            Paciente p = new Paciente(id, nombre, telefono, fechaNacimiento);

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
        view.getDatePicker().setDate(new Date());
    }


    private void cargarPacientes() {
        List<Paciente> pacientes = new ArrayList<>();
        for (Paciente p : pacienteService.leerTodos()) {
            pacientes.add(p);
        }
        tableModel.setPacientes(pacientes);
    }


    public Paciente getPacienteById(int id) {
        validarId(id);
        return pacienteService.leerPorId( id);
    }




    private void validarId(int id) {
        for (Paciente p : pacienteService.leerTodos()) {
            if (id <= 0) {
                throw new IllegalArgumentException("ID debe ser positivo");
            }
            if (p.getId() == id) {
                throw new IllegalArgumentException("ID ya existe");
            }
        }
    }

    private void validarNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty())
            throw new IllegalArgumentException("El nombre es obligatorio.");
    }

    public void generarReportePacienteSeleccionado() {
        int row = view.getPacientestable().getSelectedRow();
        if (row >= 0) {
            int id = (int) tableModel.getValueAt(row, 0);
            Paciente u = pacienteService.leerPorId(id);
            if (u != null) {
                String reporte = "Reporte de Usuario\n\n" +
                        "ID: " + u.getId() + "\n" +
                        "Nombre: " + u.getNombre() + "\n" +
                        "Tipo: " + u.getClass().getSimpleName() + "\n";
                //Proximamente agregar mas detalles sobre medicamentos

                JOptionPane.showMessageDialog(view, reporte, "Reporte de Paciente", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(view, "No se encontró el Paciente seleccionado.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(view, "Por favor, seleccione un Paciente de la tabla.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }


}




