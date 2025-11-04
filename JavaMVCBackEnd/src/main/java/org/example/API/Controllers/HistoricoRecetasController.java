package org.example.API.Controllers;

import com.google.gson.Gson;
import org.example.DataAcces.services.RecetaService;
import org.example.DataAcces.services.PacienteService;
import org.example.Domain.Dtos.RequestDto;
import org.example.Domain.Dtos.ResponseDto;
import org.example.Domain.Dtos.Receta.RecetaResponseDto;
import org.example.Domain.Dtos.DetalleReceta.DetalleRecetaResponseDto;
import org.example.Domain.Dtos.Paciente.PacienteResponseDto;
import org.example.Domain.models.Receta;
import org.example.Domain.models.Paciente;
import org.example.Domain.models.DetalleReceta;
import org.example.Utilities.EstadoReceta;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador para consultar el historial de recetas.
 * Permite buscar pacientes y ver sus recetas históricas.
 */
public class HistoricoRecetasController {
    private final RecetaService recetaService;
    private final PacienteService pacienteService;
    private final Gson gson = new Gson();
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public HistoricoRecetasController(RecetaService recetaService, PacienteService pacienteService) {
        this.recetaService = recetaService;
        this.pacienteService = pacienteService;
    }

    public ResponseDto route(RequestDto request) {
        try {
            switch (request.getRequest()) {
                case "buscarPaciente":
                case "buscarPacientePorId":
                    return handleBuscarPacientePorId(request);
                case "buscarPacientes":
                case "buscarPacientesPorNombre":
                    return handleBuscarPacientesPorNombre(request);
                case "obtenerRecetas":
                case "obtenerRecetasPorPaciente":
                    return handleObtenerRecetasPorPaciente(request);
                case "obtenerTodasRecetas":
                case "listarRecetas":
                    return handleObtenerTodasLasRecetas(request);
                case "obtenerRecetaPorId":
                    return handleObtenerRecetaPorId(request);
                case "filtrarPorEstado":
                    return handleFiltrarPorEstado(request);
                case "filtrarPorFecha":
                    return handleFiltrarPorFecha(request);
                default:
                    return new ResponseDto(false, "Unknown request: " + request.getRequest(), null);
            }
        } catch (Exception e) {
            System.err.println("[HistoricoRecetasController] Error en route: " + e.getMessage());
            e.printStackTrace();
            return new ResponseDto(false, "Error procesando petición: " + e.getMessage(), null);
        }
    }

    /**
     * Busca un paciente por su ID
     */
    private ResponseDto handleBuscarPacientePorId(RequestDto request) {
        try {
            var params = gson.fromJson(request.getData(), java.util.Map.class);
            int id = ((Number) params.get("id")).intValue();

            Paciente paciente = pacienteService.getById(Long.valueOf(id));

            if (paciente == null) {
                return new ResponseDto(false, "Paciente no encontrado", null);
            }

            PacienteResponseDto response = toPacienteResponseDto(paciente);
            return new ResponseDto(true, "Paciente encontrado", gson.toJson(response));
        } catch (Exception e) {
            System.err.println("[HistoricoRecetasController] Error en handleBuscarPacientePorId: " + e.getMessage());
            return new ResponseDto(false, "Error buscando paciente: " + e.getMessage(), null);
        }
    }

    /**
     * Busca pacientes por nombre
     */
    private ResponseDto handleBuscarPacientesPorNombre(RequestDto request) {
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
            System.err.println("[HistoricoRecetasController] Error en handleBuscarPacientesPorNombre: " + e.getMessage());
            return new ResponseDto(false, "Error buscando pacientes: " + e.getMessage(), null);
        }
    }

    /**
     * Obtiene el historial de recetas de un paciente específico
     */
    private ResponseDto handleObtenerRecetasPorPaciente(RequestDto request) {
        try {
            var params = gson.fromJson(request.getData(), java.util.Map.class);
            int pacienteId = ((Number) params.get("pacienteId")).intValue();

            List<Receta> recetas = recetaService.getByPaciente(Long.valueOf(pacienteId));
            List<RecetaResponseDto> recetaDtos = recetas.stream()
                    .map(this::toRecetaResponseDto)
                    .collect(Collectors.toList());

            System.out.println("[HistoricoRecetasController] Recetas encontradas para paciente " + pacienteId + ": " + recetaDtos.size());
            return new ResponseDto(true, "Recetas obtenidas: " + recetaDtos.size(), gson.toJson(recetaDtos));
        } catch (Exception e) {
            System.err.println("[HistoricoRecetasController] Error en handleObtenerRecetasPorPaciente: " + e.getMessage());
            return new ResponseDto(false, "Error obteniendo recetas: " + e.getMessage(), null);
        }
    }

    /**
     * Obtiene todas las recetas registradas en el sistema
     */
    private ResponseDto handleObtenerTodasLasRecetas(RequestDto request) {
        try {
            List<Receta> recetas = recetaService.getAll();
            List<RecetaResponseDto> recetaDtos = recetas.stream()
                    .map(this::toRecetaResponseDto)
                    .collect(Collectors.toList());

            System.out.println("[HistoricoRecetasController] Total de recetas: " + recetaDtos.size());
            return new ResponseDto(true, "Recetas obtenidas: " + recetaDtos.size(), gson.toJson(recetaDtos));
        } catch (Exception e) {
            System.err.println("[HistoricoRecetasController] Error en handleObtenerTodasLasRecetas: " + e.getMessage());
            return new ResponseDto(false, "Error obteniendo recetas: " + e.getMessage(), null);
        }
    }

    /**
     * Obtiene una receta específica por ID
     */
    private ResponseDto handleObtenerRecetaPorId(RequestDto request) {
        try {
            var params = gson.fromJson(request.getData(), java.util.Map.class);
            int recetaId = ((Number) params.get("id")).intValue();

            Receta receta = recetaService.getById(Long.valueOf(recetaId));

            if (receta == null) {
                return new ResponseDto(false, "Receta no encontrada", null);
            }

            RecetaResponseDto response = toRecetaResponseDto(receta);
            return new ResponseDto(true, "Receta encontrada", gson.toJson(response));
        } catch (Exception e) {
            System.err.println("[HistoricoRecetasController] Error en handleObtenerRecetaPorId: " + e.getMessage());
            return new ResponseDto(false, "Error obteniendo receta: " + e.getMessage(), null);
        }
    }

    /**
     * Filtra recetas por estado
     */
    private ResponseDto handleFiltrarPorEstado(RequestDto request) {
        try {
            var params = gson.fromJson(request.getData(), java.util.Map.class);
            String estadoStr = (String) params.get("estado");

            EstadoReceta estado;
            try {
                estado = EstadoReceta.valueOf(estadoStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                return new ResponseDto(false, "Estado inválido: " + estadoStr, null);
            }

            List<Receta> recetas = recetaService.getByEstado(estado);
            List<RecetaResponseDto> recetaDtos = recetas.stream()
                    .map(this::toRecetaResponseDto)
                    .collect(Collectors.toList());

            return new ResponseDto(true, "Recetas filtradas: " + recetaDtos.size(), gson.toJson(recetaDtos));
        } catch (Exception e) {
            System.err.println("[HistoricoRecetasController] Error en handleFiltrarPorEstado: " + e.getMessage());
            return new ResponseDto(false, "Error filtrando recetas: " + e.getMessage(), null);
        }
    }

    /**
     * Filtra recetas por rango de fechas
     */
    private ResponseDto handleFiltrarPorFecha(RequestDto request) {
        try {
            var params = gson.fromJson(request.getData(), java.util.Map.class);
            String fechaInicioStr = (String) params.get("fechaInicio");
            String fechaFinStr = (String) params.get("fechaFin");

            LocalDateTime fechaInicio = LocalDateTime.parse(fechaInicioStr, FORMATTER);
            LocalDateTime fechaFin = LocalDateTime.parse(fechaFinStr, FORMATTER);

            List<Receta> recetas = recetaService.getByFechaRange(fechaInicio, fechaFin);
            List<RecetaResponseDto> recetaDtos = recetas.stream()
                    .map(this::toRecetaResponseDto)
                    .collect(Collectors.toList());

            return new ResponseDto(true, "Recetas filtradas: " + recetaDtos.size(), gson.toJson(recetaDtos));
        } catch (Exception e) {
            System.err.println("[HistoricoRecetasController] Error en handleFiltrarPorFecha: " + e.getMessage());
            return new ResponseDto(false, "Error filtrando recetas: " + e.getMessage(), null);
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
}