package org.example.Presentation.Controllers;

import org.example.Domain.Dtos.DetalleReceta.AddDetalleRecetaRequestDto;
import org.example.Domain.Dtos.DetalleReceta.DetalleRecetaResponseDto;
import org.example.Domain.Dtos.Paciente.PacienteResponseDto;
import org.example.Domain.Dtos.Medicamento.MedicamentoResponseDto;
import org.example.Domain.Dtos.Receta.AddRecetaRequestDto;
import org.example.Domain.Dtos.Receta.RecetaResponseDto;
import org.example.Presentation.Models.DetalleRecetaTableModel;
import org.example.Presentation.Models.MedicamentoTableModel;
import org.example.Presentation.Models.PacienteTableModel;
import org.example.Presentation.Views.PrescribirForm;
import org.example.Services.PrescribirService;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class PrescribirController {
    private final PrescribirForm view;
    private final PrescribirService service;
    private final int idMedico;
    private List<PacienteResponseDto> pacientes = new ArrayList<>();
    private List<MedicamentoResponseDto> medicamentos = new ArrayList<>();

    public PrescribirController(PrescribirForm view, PrescribirService service, int idMedico) {
        this.view = view;
        this.service = service;
        this.idMedico = idMedico;
        cargarDatos();
        wireEvents();
    }

    private void cargarDatos() {
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                pacientes = service.listarPacientes();
                medicamentos = service.listarMedicamentos();
                if (pacientes == null) pacientes = new ArrayList<>();
                if (medicamentos == null) medicamentos = new ArrayList<>();
                return null;
            }

            @Override
            protected void done() {
                ((PacienteTableModel) view.getPacientesTable().getModel()).setPacientes(pacientes);
                ((MedicamentoTableModel) view.getMedicamentosTable().getModel()).setMedicamentos(medicamentos);
            }
        }.execute();
    }

    private void wireEvents() {
        view.getBuscarMainMenuPacienteButton().addActionListener(e -> view.showBuscarPacienteDialog());
        view.getAgregarMedicamentoMainMenuButton().addActionListener(e -> view.showAgregarMedicamentoDialog());
        view.getGuardarMainMenuButton().addActionListener(e -> guardarReceta());
        view.getLimpiarMainMenuButton().addActionListener(e -> limpiar());
        view.getDescartarMainMenuButton().addActionListener(e -> descartar());

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

    private void filtrarPacientes() {
        String filtro = view.getBusquedaPacientetextField().getText().trim().toLowerCase();
        String modo = (String) view.getFiltrarPacientecomboBox().getSelectedItem();

        List<PacienteResponseDto> filtrados = pacientes.stream().filter(p -> {
            if ("ID".equals(modo)) return String.valueOf(p.getId()).contains(filtro);
            return p.getNombre().toLowerCase().contains(filtro);
        }).collect(Collectors.toList());

        ((PacienteTableModel) view.getPacientesTable().getModel()).setPacientes(filtrados);
    }

    private void seleccionarPaciente() {
        int row = view.getPacientesTable().getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(view, "Seleccione un paciente");
            return;
        }
        PacienteTableModel model = (PacienteTableModel) view.getPacientesTable().getModel();
        PacienteResponseDto p = model.getPacienteAt(row);
        view.setSelectedPaciente(p);
        view.getPacientePromtPlace().setText("Paciente: " + p.getNombre());
        view.closeBuscarPacienteDialog();
    }

    private void filtrarMedicamentos() {
        String filtro = view.getBusquedaMedicamentoTextField().getText().trim().toLowerCase();
        String modo = (String) view.getFiltraMedicamentoComboBox().getSelectedItem();

        List<MedicamentoResponseDto> filtrados = medicamentos.stream().filter(m -> {
            if ("ID".equals(modo)) return String.valueOf(m.getId()).contains(filtro);
            return m.getNombre().toLowerCase().contains(filtro);
        }).collect(Collectors.toList());

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
        MedicamentoResponseDto m = view.getCurrentMedicamento();
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

        DetalleRecetaResponseDto detalle = new DetalleRecetaResponseDto();
        detalle.setIdMedicamento(m.getId());
        detalle.setCantidad(cantidad);
        detalle.setIndicaciones(indicaciones);
        detalle.setDias(dias);

        view.getDetalleRecetaList().add(detalle);
        ((DetalleRecetaTableModel) view.getMedicamentoRecetatable().getModel()).setDetalles(view.getDetalleRecetaList());
        view.closeDetalleDialog();
    }

    private void guardarReceta() {
        PacienteResponseDto paciente = view.getSelectedPaciente();
        if (paciente == null) {
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

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<AddDetalleRecetaRequestDto> detallesDto = view.getDetalleRecetaList().stream()
                .map(d -> new AddDetalleRecetaRequestDto(d.getIdMedicamento(), d.getCantidad(), d.getIndicaciones(), d.getDias()))
                .collect(Collectors.toList());

        AddRecetaRequestDto dto = new AddRecetaRequestDto(paciente.getId(), idMedico, sdf.format(view.getDatePicker().getDate()), detallesDto);

        new SwingWorker<RecetaResponseDto, Void>() {
            @Override
            protected RecetaResponseDto doInBackground() {
                return service.crearReceta(dto);
            }

            @Override
            protected void done() {
                try {
                    RecetaResponseDto resultado = get();
                    if (resultado != null) {
                        JOptionPane.showMessageDialog(view, "Receta guardada - ID: " + resultado.getId(), "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        limpiar();
                    } else {
                        JOptionPane.showMessageDialog(view, "Error al guardar receta", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(view, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }

    private void limpiar() {
        view.getDetalleRecetaList().clear();
        ((DetalleRecetaTableModel) view.getMedicamentoRecetatable().getModel()).clear();
        view.setSelectedPaciente(null);
        view.getPacientePromtPlace().setText("Paciente:");
        view.getDatePicker().setDate(new Date());
    }

    private void descartar() {
        if (JOptionPane.showConfirmDialog(view, "¿Descartar cambios?", "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            limpiar();
        }
    }
}