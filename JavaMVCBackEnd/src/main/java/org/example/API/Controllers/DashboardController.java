package org.example.API.Controllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.DataAcces.services.DashboardService;
import org.example.Domain.dtos.RequestDto;
import org.example.Domain.dtos.ResponseDto;

import java.util.List;
import java.util.Map;

/**
 * Controlador para las operaciones del Dashboard
 */
public class DashboardController {
    private final DashboardService dashboardService;
    private final Gson gson = new Gson();

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    public ResponseDto handleRequest(RequestDto request) {
        String action = request.getRequest();
        System.out.println("[DashboardController] Procesando acción: " + action);

        try {
            switch (action) {
                case "getData":
                    return getDashboardData(request);
                case "getRecetasInRange":
                    return getRecetasInRange(request);
                case "getMedicamentosPorMes":
                    return getMedicamentosPorMes(request);
                case "getRecetasPorEstado":
                    return getRecetasPorEstado(request);
                default:
                    return new ResponseDto(false, "Acción no reconocida: " + action, null);
            }
        } catch (Exception e) {
            System.err.println("[DashboardController] Error procesando petición: " + e.getMessage());
            e.printStackTrace();
            return new ResponseDto(false, "Error en el servidor: " + e.getMessage(), null);
        }
    }

    /**
     * Obtiene datos básicos del dashboard
     */
    private ResponseDto getDashboardData(RequestDto request) {
        try {
            DateRangeRequest dateRange = gson.fromJson(request.getData(), DateRangeRequest.class);
            DashboardService.DashboardData data = dashboardService.getDashboardData(
                    dateRange.getStartDate(),
                    dateRange.getEndDate()
            );
            return new ResponseDto(true, "Datos obtenidos exitosamente", gson.toJson(data));
        } catch (Exception e) {
            System.err.println("[DashboardController] Error en getData: " + e.getMessage());
            e.printStackTrace();
            return new ResponseDto(false, "Error obteniendo datos del dashboard: " + e.getMessage(), null);
        }
    }

    /**
     * Obtiene recetas en un rango de fechas
     */
    private ResponseDto getRecetasInRange(RequestDto request) {
        try {
            DateRangeRequest dateRange = gson.fromJson(request.getData(), DateRangeRequest.class);
            List<DashboardService.RecetaData> recetas = dashboardService.getRecetasInRange(
                    dateRange.getStartDate(),
                    dateRange.getEndDate()
            );
            return new ResponseDto(true, "Recetas obtenidas exitosamente", gson.toJson(recetas));
        } catch (Exception e) {
            System.err.println("[DashboardController] Error en getRecetasInRange: " + e.getMessage());
            e.printStackTrace();
            return new ResponseDto(false, "Error obteniendo recetas: " + e.getMessage(), null);
        }
    }

    /**
     * Obtiene unidades de medicamentos por mes
     */
    private ResponseDto getMedicamentosPorMes(RequestDto request) {
        try {
            MedicamentosPorMesRequest req = gson.fromJson(request.getData(), MedicamentosPorMesRequest.class);
            Map<String, Map<String, Integer>> unidades = dashboardService.getMedicamentosUnidadesPorMes(
                    req.getStartDate(),
                    req.getEndDate(),
                    req.getMedicamentoIds()
            );
            return new ResponseDto(true, "Unidades por mes obtenidas exitosamente", gson.toJson(unidades));
        } catch (Exception e) {
            System.err.println("[DashboardController] Error en getMedicamentosPorMes: " + e.getMessage());
            e.printStackTrace();
            return new ResponseDto(false, "Error obteniendo unidades por mes: " + e.getMessage(), null);
        }
    }

    /**
     * Obtiene distribución de recetas por estado
     */
    private ResponseDto getRecetasPorEstado(RequestDto request) {
        try {
            DateRangeRequest dateRange = gson.fromJson(request.getData(), DateRangeRequest.class);
            Map<String, Integer> distribucion = dashboardService.getRecetasPorEstado(
                    dateRange.getStartDate(),
                    dateRange.getEndDate()
            );
            return new ResponseDto(true, "Distribución por estado obtenida exitosamente", gson.toJson(distribucion));
        } catch (Exception e) {
            System.err.println("[DashboardController] Error en getRecetasPorEstado: " + e.getMessage());
            e.printStackTrace();
            return new ResponseDto(false, "Error obteniendo distribución por estado: " + e.getMessage(), null);
        }
    }

    // =============== DTOs de Request ===============

    private static class DateRangeRequest {
        private String startDate;
        private String endDate;

        public String getStartDate() { return startDate; }
        public void setStartDate(String startDate) { this.startDate = startDate; }
        public String getEndDate() { return endDate; }
        public void setEndDate(String endDate) { this.endDate = endDate; }
    }

    private static class MedicamentosPorMesRequest {
        private String startDate;
        private String endDate;
        private List<Long> medicamentoIds;

        public String getStartDate() { return startDate; }
        public void setStartDate(String startDate) { this.startDate = startDate; }
        public String getEndDate() { return endDate; }
        public void setEndDate(String endDate) { this.endDate = endDate; }
        public List<Long> getMedicamentoIds() { return medicamentoIds; }
        public void setMedicamentoIds(List<Long> medicamentoIds) { this.medicamentoIds = medicamentoIds; }
    }
}