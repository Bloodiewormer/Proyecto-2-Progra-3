package org.example.API.Controllers;

import com.google.gson.Gson;
import org.example.DataAcces.services.RecetaService;
import org.example.DataAcces.services.PacienteService;
import org.example.DataAcces.services.MedicamentoService;
import org.example.Domain.Dtos.RequestDto;
import org.example.Domain.Dtos.ResponseDto;
import org.example.Domain.Dtos.Receta.AddRecetaRequestDto;
import org.example.Domain.Dtos.Receta.RecetaResponseDto;
import org.example.Domain.Dtos.DetalleReceta.AddDetalleRecetaRequestDto;
import org.example.Domain.Dtos.DetalleReceta.DetalleRecetaResponseDto;
import org.example.Domain.Dtos.Paciente.PacienteResponseDto;
import org.example.Domain.Dtos.Medicamento.MedicamentoResponseDto;
import org.example.Domain.models.Receta;
import org.example.Domain.models.Paciente;
import org.example.Domain.models.Medicamento;
import org.example.Domain.models.DetalleReceta;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador para la prescripción de recetas médicas.
 * Maneja la creación de recetas con sus detalles y búsqueda de pacientes/medicamentos.
 */
public class PrescribirController {
    private final RecetaService recetaService;
    private final PacienteService pacienteService;
    private final MedicamentoService medicamentoService;
    private final Gson gson = new Gson();
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public PrescribirController(RecetaService recetaService,
                                PacienteService pacienteService,
                                MedicamentoService medicamentoService) {
        this.recetaService = recetaService;
        this.pacienteService = pacienteService;
        this.medicamentoService = medicamentoService;
    }

    public ResponseDto route(RequestDto request) {
        try {
            switch (request.getRequest()) {
                case "registrar":
                case "crear":
                    return handleRegistrarReceta(request);
                case "buscarPaciente":
                    return handleBuscarPaciente(request);
                case "buscarMedicamento":
                    return handleBuscarMedicamento(request);
                case "listarPacientes":
                    return handleListarPacientes(request);
                case "listarMedicamentos":
                    return handleListarMedicamentos(request);
                default:
                    return new ResponseDto(false, "Unknown request: " + request.getRequest(), null);
            }
        } catch (Exception e) {
            System.err.println("[PrescribirController] Error en route: " + e.getMessage());
            e.printStackTrace();
            return new ResponseDto(false, "Error procesando petición: " + e.getMessage(), null);
        }
    }

    /**
     * Registra una nueva receta con sus detalles
     */
    private ResponseDto handleRegistrarReceta(RequestDto request) {
        try {
            if (request.getData() == null || request.getData().isEmpty()) {
                return new ResponseDto(false, "Datos de receta vacíos", null);
            }

            AddRecetaRequestDto dto = gson.fromJson(request.getData(), AddRecetaRequestDto.class);

            // Validaciones
            if (dto.getIdPaciente() <= 0 || dto.getIdMedico() <= 0) {
                return new ResponseDto(false, "ID de paciente o médico inválido", null);
            }

            if (dto.getDetalles() == null || dto.getDetalles().isEmpty()) {
                return new ResponseDto(false, "La receta debe tener al menos un medicamento", null);
            }

            // Parsear fecha de retiro (si viene como String)
            LocalDateTime fechaRetiro;
            try {
                if (dto.getFechaConfeccion() != null && !dto.getFechaConfeccion().isEmpty()) {
                    fechaRetiro = LocalDateTime.parse(dto.getFechaConfeccion(), FORMATTER);
                } else {
                    // Si no viene fecha, usar fecha actual + 30 días
                    fechaRetiro = LocalDateTime.now().plusDays(30);
                }
            } catch (Exception e) {
                fechaRetiro = LocalDateTime.now().plusDays(30);
            }

            // Crear la receta
            Receta receta = recetaService.create(
                    Long.valueOf(dto.getIdPaciente()),
                    Long.valueOf(dto.getIdMedico()),
                    fechaRetiro
            );

            if (receta == null) {
                return new ResponseDto(false, "No se pudo crear la receta. Verifique que el paciente y médico existan.", null);
            }

            // Agregar detalles
            for (AddDetalleRecetaRequestDto detalle : dto.getDetalles()) {
                if (detalle.getIdMedicamento() <= 0 || detalle.getCantidad() <= 0) {
                    continue; // Saltar detalles inválidos
                }

                recetaService.addDetalle(
                        receta.getId(),
                        Long.valueOf(detalle.getIdMedicamento()),
                        detalle.getCantidad(),
                        detalle.getIndicaciones() != null ? detalle.getIndicaciones() : "",
                        detalle.getDias() > 0 ? detalle.getDias() : 30
                );
            }

            // Obtener la receta completa con detalles
            receta = recetaService.getById(receta.getId());
            RecetaResponseDto response = toRecetaResponseDto(receta);

            System.out.println("[PrescribirController] Receta prescrita exitosamente - ID: " + receta.getId());
            return new ResponseDto(true, "Receta prescrita exitosamente", gson.toJson(response));

        } catch (Exception e) {
            System.err.println("[PrescribirController] Error en handleRegistrarReceta: " + e.getMessage());
            e.printStackTrace();
            return new ResponseDto(false, "Error registrando receta: " + e.getMessage(), null);
        }
    }

    /**
     * Busca pacientes por nombre
     */
    private ResponseDto handleBuscarPaciente(RequestDto request) {
        try {
            var params = gson.fromJson(request.getData(), java.util.Map.class);
            String nombre = (String) params.get("nombre");

            if (nombre == null || nombre.trim().isEmpty()) {
                return new ResponseDto(false, "Debe proporcionar un nombre para buscar", null);
            }

            List<Paciente> pacientes = pacienteService.buscarPorNombre(nombre);
            List<PacienteResponseDto> pacienteDtos = pacientes.stream()
                    .map(this::toPacienteResponseDto)
                    .collect(Collectors.toList());

            return new ResponseDto(true, "Pacientes encontrados: " + pacienteDtos.size(), gson.toJson(pacienteDtos));
        } catch (Exception e) {
            System.err.println("[PrescribirController] Error en handleBuscarPaciente: " + e.getMessage());
            return new ResponseDto(false, "Error buscando paciente: " + e.getMessage(), null);
        }
    }

    /**
     * Busca medicamentos por nombre
     */
    private ResponseDto handleBuscarMedicamento(RequestDto request) {
        try {
            var params = gson.fromJson(request.getData(), java.util.Map.class);
            String nombre = (String) params.get("nombre");

            if (nombre == null || nombre.trim().isEmpty()) {
                return new ResponseDto(false, "Debe proporcionar un nombre para buscar", null);
            }

            List<Medicamento> medicamentos = medicamentoService.buscarPorNombre(nombre);
            List<MedicamentoResponseDto> medicamentoDtos = medicamentos.stream()
                    .map(this::toMedicamentoResponseDto)
                    .collect(Collectors.toList());

            return new ResponseDto(true, "Medicamentos encontrados: " + medicamentoDtos.size(), gson.toJson(medicamentoDtos));
        } catch (Exception e) {
            System.err.println("[PrescribirController] Error en handleBuscarMedicamento: " + e.getMessage());
            return new ResponseDto(false, "Error buscando medicamento: " + e.getMessage(), null);
        }
    }

    /**
     * Lista todos los pacientes
     */
    private ResponseDto handleListarPacientes(RequestDto request) {
        try {
            List<Paciente> pacientes = pacienteService.getAll();
            List<PacienteResponseDto> pacienteDtos = pacientes.stream()
                    .map(this::toPacienteResponseDto)
                    .collect(Collectors.toList());

            return new ResponseDto(true, "Pacientes obtenidos: " + pacienteDtos.size(), gson.toJson(pacienteDtos));
        } catch (Exception e) {
            System.err.println("[PrescribirController] Error en handleListarPacientes: " + e.getMessage());
            return new ResponseDto(false, "Error listando pacientes: " + e.getMessage(), null);
        }
    }

    /**
     * Lista todos los medicamentos
     */
    private ResponseDto handleListarMedicamentos(RequestDto request) {
        try {
            List<Medicamento> medicamentos = medicamentoService.getAll();
            List<MedicamentoResponseDto> medicamentoDtos = medicamentos.stream()
                    .map(this::toMedicamentoResponseDto)
                    .collect(Collectors.toList());

            return new ResponseDto(true, "Medicamentos obtenidos: " + medicamentoDtos.size(), gson.toJson(medicamentoDtos));
        } catch (Exception e) {
            System.err.println("[PrescribirController] Error en handleListarMedicamentos: " + e.getMessage());
            return new ResponseDto(false, "Error listando medicamentos: " + e.getMessage(), null);
        }
    }

    // ===== Métodos helper para conversión =====

    private RecetaResponseDto toRecetaResponseDto(Receta receta) {
        List<DetalleRecetaResponseDto> detalles = receta.getDetalles().stream()
                .map(this::toDetalleResponseDto)
                .collect(Collectors.toList());

        String fechaRetiroStr = receta.getFechaRetiro() != null
                ? receta.getFechaRetiro().format(FORMATTER)
                : null;


        return new RecetaResponseDto(
                receta.getId().intValue(),
                receta.getPaciente().getId().intValue(),
                receta.getMedico().getId().intValue(),
                receta.getPaciente().getNombre(),
                receta.getMedico().getNombre(),
                receta.getFechaConfeccion().format(FORMATTER),
                fechaRetiroStr,
                receta.getEstado().toString(),
                detalles
        );
    }

    private DetalleRecetaResponseDto toDetalleResponseDto(DetalleReceta detalle) {
        return new DetalleRecetaResponseDto(
                detalle.getId().intValue(),
                detalle.getMedicamento().getId().intValue(),
                detalle.getCantidad(),
                detalle.getIndicaciones(),
                detalle.getDias()
        );
    }

    private PacienteResponseDto toPacienteResponseDto(Paciente paciente) {
        return new PacienteResponseDto(
                paciente.getId().intValue(),
                paciente.getNombre(),
                paciente.getTelefono(),
                paciente.getFechaNacimiento().toString()
        );
    }

    private MedicamentoResponseDto toMedicamentoResponseDto(Medicamento medicamento) {
        return new MedicamentoResponseDto(
                medicamento.getId().intValue(),
                medicamento.getNombre(),
                medicamento.getPresentacion()
        );
    }
}