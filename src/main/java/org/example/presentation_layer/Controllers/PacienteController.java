package org.example.presentation_layer.Controllers;

import org.example.domain_layer.Paciente;
import org.example.presentation_layer.Models.PacienteTableModel;
import org.example.service_layer.IService;

import java.util.Date;
import java.util.List;

public class PacienteController {

    private final IService<Paciente> pacienteService;
    private final PacienteTableModel tableModel;

    public PacienteController(IService<Paciente> pacienteService) {
        this.pacienteService = pacienteService;
        this.tableModel = new PacienteTableModel(pacienteService.leerTodos());
    }

    public PacienteTableModel getTableModel() {
        return tableModel;
    }

    public void guardarPaciente(int id, String nombre, String telefono, Date fechaNacimiento) {
        Paciente nuevo = new Paciente(id, nombre, telefono, fechaNacimiento);
        pacienteService.agregar(nuevo);
        refrescarTabla();
    }

    public void actualizarPaciente(Paciente paciente) {
        pacienteService.actualizar(paciente);
        refrescarTabla();
    }

    public void borrarPaciente(int id) {
        pacienteService.borrar(id);
        refrescarTabla();
    }

    public Paciente obtenerPacientePorId(int id) {
        return pacienteService.leerPorId(id);
    }

    public List<Paciente> obtenerTodos() {
        return pacienteService.leerTodos();
    }

    private void refrescarTabla() {
        List<Paciente> pacientes = pacienteService.leerTodos();
        tableModel.setMedicos(pacientes); // ver si hay que corregir algo xd
    }
}

