package org.example.API.Controllers;

import com.google.gson.Gson;
import org.example.DataAcces.services.UsuarioService;
import org.example.Domain.Dtos.Farmaceuta.*;
import org.example.Domain.Dtos.RequestDto;
import org.example.Domain.Dtos.ResponseDto;
import org.example.Domain.models.Farmaceuta;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador para operaciones CRUD de Farmacéuticos
 * Modelo igual a MedicoController
 */
public class FarmaceutaController {

    private final UsuarioService usuarioService;
    private final Gson gson = new Gson();

    public FarmaceutaController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * Enruta las peticiones a sus métodos correspondientes
     */
    public ResponseDto route(RequestDto request) {
        try {
            if (request.getRequest() == null) {
                return new ResponseDto(false, "Petición sin tipo de request", null);
            }

            switch (request.getRequest()) {
                case "listar":
                case "list":
                    return handleListar();
                case "agregar":
                case "add":
                    return handleAgregar(request);
                case "actualizar":
                case "update":
                    return handleActualizar(request);
                case "eliminar":
                case "delete":
                    return handleEliminar(request);
                case "buscarPorNombre":
                case "search":
                    return handleBuscarPorNombre(request);
                default:
                    return new ResponseDto(false, "Petición desconocida: " + request.getRequest(), null);
            }
        } catch (Exception e) {
            System.err.println("[FarmaceutaController] Error en route: " + e.getMessage());
            e.printStackTrace();
            return new ResponseDto(false, "Error interno: " + e.getMessage(), null);
        }
    }

    /**
     * Lista todos los farmacéuticos
     */
    private ResponseDto handleListar() {
        try {
            List<Farmaceuta> farmaceutas = usuarioService.getAllFarmaceutas();

            List<FarmaceutaResponseDto> farmaceutaDtos = farmaceutas.stream()
                    .map(f -> new FarmaceutaResponseDto(
                            f.getId().intValue(),
                            f.getNombre()
                    ))
                    .collect(Collectors.toList());

            ListFarmaceutaResponseDto listResponse = new ListFarmaceutaResponseDto(farmaceutaDtos);

            System.out.println("[FarmaceutaController] Farmacéuticos listados: " + farmaceutaDtos.size());
            return new ResponseDto(true, "Farmacéuticos obtenidos exitosamente", gson.toJson(listResponse));

        } catch (Exception e) {
            System.err.println("[FarmaceutaController] Error en handleListar: " + e.getMessage());
            e.printStackTrace();
            return new ResponseDto(false, "Error al listar farmacéuticos: " + e.getMessage(), null);
        }
    }

    /**
     * Agrega un nuevo farmacéutico
     */
    private ResponseDto handleAgregar(RequestDto request) {
        try {
            AddFarmaceutaRequestDto dto = gson.fromJson(request.getData(), AddFarmaceutaRequestDto.class);

            if (dto.getNombre() == null || dto.getNombre().trim().isEmpty()) {
                return new ResponseDto(false, "El nombre es requerido", null);
            }

            // Crear farmacéutico con contraseña por defecto
            Farmaceuta farmaceuta = usuarioService.createFarmaceuta(
                    dto.getNombre(),
                    dto.getClave() != null ? dto.getClave() : "temp123"
            );

            FarmaceutaResponseDto responseDto = new FarmaceutaResponseDto(
                    farmaceuta.getId().intValue(),
                    farmaceuta.getNombre()
            );

            System.out.println("[FarmaceutaController] Farmacéutico creado: " + farmaceuta.getNombre() + " (ID: " + farmaceuta.getId() + ")");
            return new ResponseDto(true, "Farmacéutico agregado exitosamente", gson.toJson(responseDto));

        } catch (IllegalArgumentException e) {
            System.err.println("[FarmaceutaController] Error de validación: " + e.getMessage());
            return new ResponseDto(false, e.getMessage(), null);
        } catch (Exception e) {
            System.err.println("[FarmaceutaController] Error en handleAgregar: " + e.getMessage());
            e.printStackTrace();
            return new ResponseDto(false, "Error al agregar farmacéutico: " + e.getMessage(), null);
        }
    }

    /**
     * Actualiza un farmacéutico existente
     */
    private ResponseDto handleActualizar(RequestDto request) {
        try {
            UpdateFarmaceutaRequestDto dto = gson.fromJson(request.getData(), UpdateFarmaceutaRequestDto.class);

            Farmaceuta farmaceuta = usuarioService.updateFarmaceuta(
                    (long) dto.getId(),
                    dto.getNombre(),
                    dto.getClave()
            );

            if (farmaceuta == null) {
                return new ResponseDto(false, "Farmacéutico no encontrado con ID: " + dto.getId(), null);
            }

            FarmaceutaResponseDto responseDto = new FarmaceutaResponseDto(
                    farmaceuta.getId().intValue(),
                    farmaceuta.getNombre()
            );

            System.out.println("[FarmaceutaController] Farmacéutico actualizado: " + farmaceuta.getNombre() + " (ID: " + farmaceuta.getId() + ")");
            return new ResponseDto(true, "Farmacéutico actualizado exitosamente", gson.toJson(responseDto));

        } catch (Exception e) {
            System.err.println("[FarmaceutaController] Error en handleActualizar: " + e.getMessage());
            e.printStackTrace();
            return new ResponseDto(false, "Error al actualizar farmacéutico: " + e.getMessage(), null);
        }
    }

    /**
     * Elimina un farmacéutico
     */
    private ResponseDto handleEliminar(RequestDto request) {
        try {
            DeleteFarmaceutaRequestDto dto = gson.fromJson(request.getData(), DeleteFarmaceutaRequestDto.class);

            boolean eliminado = usuarioService.deleteFarmaceuta((long) dto.getId());

            if (eliminado) {
                System.out.println("[FarmaceutaController] Farmacéutico eliminado con ID: " + dto.getId());
                return new ResponseDto(true, "Farmacéutico eliminado exitosamente", null);
            } else {
                return new ResponseDto(false, "Farmacéutico no encontrado con ID: " + dto.getId(), null);
            }

        } catch (Exception e) {
            System.err.println("[FarmaceutaController] Error en handleEliminar: " + e.getMessage());
            e.printStackTrace();
            return new ResponseDto(false, "Error al eliminar farmacéutico: " + e.getMessage(), null);
        }
    }

    /**
     * Busca farmacéuticos por nombre
     */
    private ResponseDto handleBuscarPorNombre(RequestDto request) {
        try {
            var params = gson.fromJson(request.getData(), java.util.Map.class);
            String nombre = (String) params.get("nombre");

            if (nombre == null || nombre.trim().isEmpty()) {
                return handleListar();
            }

            List<Farmaceuta> farmaceutas = usuarioService.getAllFarmaceutas();

            List<FarmaceutaResponseDto> filtrados = farmaceutas.stream()
                    .filter(f -> f.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                    .map(f -> new FarmaceutaResponseDto(
                            f.getId().intValue(),
                            f.getNombre()
                    ))
                    .collect(Collectors.toList());

            ListFarmaceutaResponseDto listResponse = new ListFarmaceutaResponseDto(filtrados);

            System.out.println("[FarmaceutaController] Búsqueda de farmacéuticos: " + filtrados.size() + " encontrados");
            return new ResponseDto(true, "Farmacéuticos filtrados", gson.toJson(listResponse));

        } catch (Exception e) {
            System.err.println("[FarmaceutaController] Error en handleBuscarPorNombre: " + e.getMessage());
            e.printStackTrace();
            return new ResponseDto(false, "Error al buscar farmacéuticos: " + e.getMessage(), null);
        }
    }
}