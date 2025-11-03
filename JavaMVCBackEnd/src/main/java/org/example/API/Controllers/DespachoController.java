package org.example.API.Controllers;

import com.google.gson.Gson;
import org.example.DataAcces.services.RecetaService;
import org.example.DataAcces.services.PacienteService;
import org.example.Domain.dtos.Paciente.PacienteResponseDto;
import org.example.Domain.dtos.Receta.RecetaResponseDto;
import org.example.Domain.dtos.Receta.UpdateRecetaRequestDto;
import org.example.Domain.dtos.RequestDto;
import org.example.Domain.dtos.ResponseDto;
import org.example.Domain.dtos.DetalleReceta.DetalleRecetaResponseDto;
import org.example.Domain.models.DetalleReceta;
import org.example.Domain.models.Paciente;
import org.example.Domain.models.Receta;
import org.example.Utilities.EstadoReceta;
import org.hibernate.Hibernate;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class DespachoController {
    private final RecetaService recetaService;
    private final PacienteService pacienteService;
    private final Gson gson = new Gson();
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public DespachoController(RecetaService recetaService, PacienteService pacienteService) {
        this.recetaService = recetaService;
        this.pacienteService = pacienteService;
    }

    public ResponseDto route(RequestDto request) {
        try {
            return switch (request.getRequest()) {
                case "listarRecetas" -> handleListarRecetas();
                case "listarPorEstado" -> handleListarPorEstado(request);
                case "listarPorPaciente" -> handleListarPorPaciente(request);
                case "actualizarEstado" -> handleActualizarEstado(request);
                case "buscarPaciente" -> handleBuscarPaciente(request);
                default -> new ResponseDto(false, "Operación desconocida: " + request.getRequest(), null);
            };
        } catch (Exception e) {
            System.err.println("[DespachoController] Error en route: " + e.getMessage());
            e.printStackTrace();
            return new ResponseDto(false, "Error procesando solicitud: " + e.getMessage(), null);
        }
    }

    private ResponseDto handleListarRecetas() {
        try {
            List<Receta> recetas = recetaService.getByEstado(EstadoReceta.CONFECCIONADA);
            recetas.addAll(recetaService.getByEstado(EstadoReceta.PROCESO));
            recetas.addAll(recetaService.getByEstado(EstadoReceta.LISTA));

            List<RecetaResponseDto> recetaDtos = recetas.stream()
                    .map(this::toRecetaResponseDto)
                    .collect(Collectors.toList());

            return new ResponseDto(true, "Recetas obtenidas: " + recetaDtos.size(), gson.toJson(recetaDtos));
        } catch (Exception e) {
            System.err.println("[DespachoController] Error en handleListarRecetas: " + e.getMessage());
            return new ResponseDto(false, "Error listando recetas: " + e.getMessage(), null);
        }
    }

    private ResponseDto handleListarPorEstado(RequestDto request) {
        try {
            String estadoStr = request.getData();
            EstadoReceta estado = EstadoReceta.valueOf(estadoStr);

            List<Receta> recetas = recetaService.getByEstado(estado);
            List<RecetaResponseDto> recetaDtos = recetas.stream()
                    .map(this::toRecetaResponseDto)
                    .collect(Collectors.toList());

            return new ResponseDto(true, "Recetas obtenidas: " + recetaDtos.size(), gson.toJson(recetaDtos));
        } catch (Exception e) {
            System.err.println("[DespachoController] Error en handleListarPorEstado: " + e.getMessage());
            return new ResponseDto(false, "Error listando recetas por estado: " + e.getMessage(), null);
        }
    }

    private ResponseDto handleListarPorPaciente(RequestDto request) {
        try {
            Long pacienteId = Long.parseLong(request.getData());
            List<Receta> recetas = recetaService.getByPaciente(pacienteId);
            List<RecetaResponseDto> recetaDtos = recetas.stream()
                    .map(this::toRecetaResponseDto)
                    .collect(Collectors.toList());

            return new ResponseDto(true, "Recetas del paciente obtenidas: " + recetaDtos.size(), gson.toJson(recetaDtos));
        } catch (Exception e) {
            System.err.println("[DespachoController] Error en handleListarPorPaciente: " + e.getMessage());
            return new ResponseDto(false, "Error listando recetas por paciente: " + e.getMessage(), null);
        }
    }

    private ResponseDto handleActualizarEstado(RequestDto request) {
        try {
            UpdateRecetaRequestDto dto = gson.fromJson(request.getData(), UpdateRecetaRequestDto.class);
            EstadoReceta nuevoEstado = EstadoReceta.valueOf(dto.getEstado());

            Receta receta = recetaService.updateEstado((long) dto.getId(), nuevoEstado);

            if (receta != null) {
                RecetaResponseDto responseDto = toRecetaResponseDto(receta);
                return new ResponseDto(true, "Estado actualizado exitosamente", gson.toJson(responseDto));
            } else {
                return new ResponseDto(false, "Receta no encontrada", null);
            }
        } catch (Exception e) {
            System.err.println("[DespachoController] Error en handleActualizarEstado: " + e.getMessage());
            return new ResponseDto(false, "Error actualizando estado: " + e.getMessage(), null);
        }
    }

    private ResponseDto handleBuscarPaciente(RequestDto request) {
        try {
            Long pacienteId = Long.parseLong(request.getData());
            Paciente paciente = pacienteService.getById(pacienteId);

            if (paciente == null) {
                return new ResponseDto(false, "Paciente no encontrado", null);
            }

            PacienteResponseDto dto = toPacienteResponseDto(paciente);
            return new ResponseDto(true, "Paciente encontrado", gson.toJson(dto));
        } catch (Exception e) {
            System.err.println("[DespachoController] Error en handleBuscarPaciente: " + e.getMessage());
            return new ResponseDto(false, "Error buscando paciente: " + e.getMessage(), null);
        }
    }

    private RecetaResponseDto toRecetaResponseDto(Receta receta) {
        List<DetalleRecetaResponseDto> detalles = receta.getDetalles().stream()
                .map(this::toDetalleResponseDto)
                .collect(Collectors.toList());

        return new RecetaResponseDto(
                receta.getId().intValue(),
                receta.getPaciente().getId().intValue(),
                receta.getMedico().getId().intValue(),
                receta.getFechaConfeccion().format(FORMATTER),
                detalles,
                receta.getEstado().name()
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
                paciente.getFechaNacimiento() != null ? paciente.getFechaNacimiento().toString() : ""
        );
    }
}