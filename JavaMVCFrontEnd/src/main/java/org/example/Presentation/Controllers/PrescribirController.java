package org.example.presentation_layer.Controllers;

import org.example.Domain.Dtos.receta.DetalleReceta;
import org.example.Domain.Dtos.medicamento.Medicamento;
import org.example.domain_layer.Paciente;
import org.example.Domain.Dtos.receta.Receta;
import org.example.presentation_layer.Models.DetalleRecetaTableModel;
import org.example.presentation_layer.Models.MedicamentoTableModel;
import org.example.presentation_layer.Models.PacienteTableModel;
import org.example.presentation_layer.Views.PrescribirForm;
import org.example.service_layer.MedicamentoService;
import org.example.service_layer.PacienteService;
import org.example.service_layer.RecetaService;
import org.example.utilities.EstadoReceta;

import javax.swing.*;
import java.util.Date;
import java.util.List;

public class PrescribirController {

    private final PrescribirForm view;
    private final PacienteService pacienteService;
    private final MedicamentoService medicamentoService;
    private final RecetaService recetaService;
    private final int idMedico;

    public PrescribirController(PrescribirForm view,
                                org.example.service_layer.UsuarioService usuarioService,
                                PacienteService pacienteService,
                                MedicamentoService medicamentoService,
                                RecetaService recetaService,
                                int idMedico) {
        this.view = view;
        this.pacienteService = pacienteService;
        this.medicamentoService = medicamentoService;
        this.recetaService = recetaService;
        this.idMedico = idMedico;
        wireEvents();
    }

    private void wireEvents() {
        view.getBuscarMainMenuPacienteButton().addActionListener(e -> view.showBuscarPacienteDialog());
        view.getAgregarMedicamentoMainMenuButton().addActionListener(e -> view.showAgregarMedicamentoDialog());
        view.getGuardarMainMenuButton().addActionListener(e -> guardarReceta());
        view.getLimpiarMainMenuButton().addActionListener(e -> limpiar());
        view.getDescartarMainMenuButton().addActionListener(e -> descartar());

        // Dialog buttons
        if (view.getBuscarPacientMenuButton() != null)
            view.getBuscarPacientMenuButton().addActionListener(e -> filtrarPacientes());
        if (view.getGuardarPacienteMenuButton() != null)
            view.getGuardarPacienteMenuButton().addActionListener(e -> seleccionarPaciente());
        if (view.getCancePacienteMenulButton() != null)
            view.getCancePacienteMenulButton().addActionListener(e -> view.closeBuscarPacienteDialog());

        if (view.getBuscarMedicamentoMenuButton() != null)
            view.getBuscarMedicamentoMenuButton().addActionListener(e -> filtrarMedicamentos());
        if (view.getGuardarMedicamentoMenuButton() != null)
            view.getGuardarMedicamentoMenuButton().addActionListener(e -> seleccionarMedicamento());
        if (view.getCancelMedicamentoMenuButton() != null)
            view.getCancelMedicamentoMenuButton().addActionListener(e -> view.closeAgregarMedicamentoDialog());

        if (view.getGuardarDetallesMenuButton() != null)
            view.getGuardarDetallesMenuButton().addActionListener(e -> agregarDetalle());
        if (view.getCancerlarButton() != null)
            view.getCancerlarButton().addActionListener(e -> view.closeDetalleDialog());
    }

    // Actions
    private void filtrarPacientes() {
        String filtro = view.getBusquedaPacientetextField().getText().trim().toLowerCase();
        String modo = (String) view.getFiltrarPacientecomboBox().getSelectedItem();
        List<Paciente> todos = pacienteService.leerTodos();
        List<Paciente> filtrados = todos.stream().filter(p -> {
            if ("ID".equals(modo)) return String.valueOf(p.getId()).contains(filtro);
            return p.getNombre().toLowerCase().contains(filtro);
        }).toList();
        ((PacienteTableModel) view.getPacientesTable().getModel()).setPacientes(filtrados);
    }

    private void seleccionarPaciente() {
        int row = view.getPacientesTable().getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(view, "Seleccione un paciente");
            return;
        }
        PacienteTableModel model = (PacienteTableModel) view.getPacientesTable().getModel();
        Paciente p = model.getPacienteAt(row);
        view.setSelectedPaciente(p);
        view.getPacientePromtPlace().setText("Paciente: " + p.getNombre());
        view.closeBuscarPacienteDialog();
    }

    private void filtrarMedicamentos() {
        String filtro = view.getBusquedaMedicamentoTextField().getText().trim().toLowerCase();
        String modo = (String) view.getFiltraMedicamentoComboBox().getSelectedItem();
        List<Medicamento> todos = medicamentoService.leerTodos();
        List<Medicamento> filtrados = todos.stream().filter(m -> {
            if ("ID".equals(modo)) return String.valueOf(m.getCodigo()).contains(filtro);
            return m.getNombre().toLowerCase().contains(filtro);
        }).toList();
        ((MedicamentoTableModel) view.getMedicamentosTable().getModel()).setMedicamentos(filtrados);
    }

    private void seleccionarMedicamento() {
        int row = view.getMedicamentosTable().getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(view, "Seleccione un medicamento");
            return;
        }
        MedicamentoTableModel model = (MedicamentoTableModel) view.getMedicamentosTable().getModel();
        view.setCurrentMedicamento(model.getMedicamentoAt(row));
        view.closeAgregarMedicamentoDialog();
        view.showDetalleDialog();
    }

    private void agregarDetalle() {
        Medicamento m = view.getCurrentMedicamento();
        if (m == null) {
            JOptionPane.showMessageDialog(view, "No hay medicamento seleccionado");
            return;
        }
        int cantidad = (int) view.getCatidadSpinner().getValue();
        int dias = (int) view.getDuracionSpinner().getValue();
        String indicaciones = view.getIndicacionesField().getText().trim();
        if (cantidad <= 0 || dias <= 0) {
            JOptionPane.showMessageDialog(view, "Cantidad y días deben ser mayores a 0");
            return;
        }
        DetalleReceta detalle = new DetalleReceta(m, cantidad, indicaciones,dias);
        view.getDetalleRecetaList().add(detalle);
        ((DetalleRecetaTableModel) view.getMedicamentoRecetatable().getModel()).fireTableDataChanged();
        view.closeDetalleDialog();
    }

    private void guardarReceta() {
        if (view.getSelectedPaciente() == null) {
            JOptionPane.showMessageDialog(view, "Seleccione un paciente");
            return;
        }
        if (view.getDetalleRecetaList().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Agregue al menos un medicamento");
            return;
        }
        if (view.getDatePicker().getDate() == null) {
            JOptionPane.showMessageDialog(view, "Seleccione fecha de retiro");
            return;
        }

        Receta r = new Receta(recetaService.getNextId(), view.getSelectedPaciente().getId(), idMedico, new Date(), view.getDatePicker().getDate(), EstadoReceta.PROCESO.toString());
        r.setDetalles(view.getDetalleRecetaList());
        recetaService.agregar(r);


        JOptionPane.showMessageDialog(view, "Receta guardada");
        limpiar();
    }

    private void limpiar() {
        view.getDetalleRecetaList().clear();
        ((DetalleRecetaTableModel) view.getMedicamentoRecetatable().getModel()).fireTableDataChanged();
        view.setSelectedPaciente(null);
        view.getPacientePromtPlace().setText("Paciente:");
        view.getDatePicker().setDate(new java.util.Date());
    }

    private void descartar() {
        int op = JOptionPane.showConfirmDialog(view, "Descartar cambios?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (op == JOptionPane.YES_OPTION) limpiar();
    }
}
