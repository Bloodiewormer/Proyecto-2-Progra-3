package org.example.Services;

import com.google.gson.reflect.TypeToken;
import org.example.Domain.Dtos.RequestDto;
import org.example.Domain.Dtos.ResponseDto;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Servicio proxy para comunicarse con el Dashboard del Backend
 */
public class DashboardService extends BaseService {
    private final ExecutorService executor = Executors.newThreadPerTaskExecutor(Thread.ofVirtual().factory());

    public DashboardService(String host, int port) {
        super(host, port);
    }

    /**
     * Obtiene datos básicos del dashboard
     */
    public Future<DashboardDataDto> getDashboardDataAsync(String startDate, String endDate) {
        return executor.submit(() -> {
            RequestDto request = new RequestDto(
                    "Dashboard",
                    "getData",
                    gson.toJson(new DateRangeDto(startDate, endDate)),
                    null
            );
            ResponseDto response = sendRequest(request);
            if (!response.isSuccess()) {
                System.err.println("[DashboardService] Error: " + response.getMessage());
                return null;
            }
            return gson.fromJson(response.getData(), DashboardDataDto.class);
        });
    }

    /**
     * Obtiene recetas en un rango de fechas
     */
    public Future<List<RecetaDataDto>> getRecetasInRangeAsync(String startDate, String endDate) {
        return executor.submit(() -> {
            RequestDto request = new RequestDto(
                    "Dashboard",
                    "getRecetasInRange",
                    gson.toJson(new DateRangeDto(startDate, endDate)),
                    null
            );
            ResponseDto response = sendRequest(request);
            if (!response.isSuccess()) {
                System.err.println("[DashboardService] Error: " + response.getMessage());
                return null;
            }
            return gson.fromJson(response.getData(), new TypeToken<List<RecetaDataDto>>(){}.getType());
        });
    }

    /**
     * Obtiene unidades de medicamentos por mes
     */
    public Future<Map<String, Map<String, Integer>>> getMedicamentosPorMesAsync(
            String startDate,
            String endDate,
            List<Long> medicamentoIds) {

        return executor.submit(() -> {
            RequestDto request = new RequestDto(
                    "Dashboard",
                    "getMedicamentosPorMes",
                    gson.toJson(new MedicamentosPorMesDto(startDate, endDate, medicamentoIds)),
                    null
            );
            ResponseDto response = sendRequest(request);
            if (!response.isSuccess()) {
                System.err.println("[DashboardService] Error: " + response.getMessage());
                return null;
            }
            return gson.fromJson(response.getData(),
                    new TypeToken<Map<String, Map<String, Integer>>>(){}.getType());
        });
    }

    /**
     * Obtiene distribución de recetas por estado
     */
    public Future<Map<String, Integer>> getRecetasPorEstadoAsync(String startDate, String endDate) {
        return executor.submit(() -> {
            RequestDto request = new RequestDto(
                    "Dashboard",
                    "getRecetasPorEstado",
                    gson.toJson(new DateRangeDto(startDate, endDate)),
                    null
            );
            ResponseDto response = sendRequest(request);
            if (!response.isSuccess()) {
                System.err.println("[DashboardService] Error: " + response.getMessage());
                return null;
            }
            return gson.fromJson(response.getData(), new TypeToken<Map<String, Integer>>(){}.getType());
        });
    }

    // =============== DTOs internos ===============

    private static class DateRangeDto {
        private final String startDate;
        private final String endDate;

        public DateRangeDto(String startDate, String endDate) {
            this.startDate = startDate;
            this.endDate = endDate;
        }

        public String getStartDate() { return startDate; }
        public String getEndDate() { return endDate; }
    }

    private static class MedicamentosPorMesDto {
        private final String startDate;
        private final String endDate;
        private final List<Long> medicamentoIds;

        public MedicamentosPorMesDto(String startDate, String endDate, List<Long> medicamentoIds) {
            this.startDate = startDate;
            this.endDate = endDate;
            this.medicamentoIds = medicamentoIds;
        }

        public String getStartDate() { return startDate; }
        public String getEndDate() { return endDate; }
        public List<Long> getMedicamentoIds() { return medicamentoIds; }
    }

    // =============== DTOs de respuesta ===============

    public static class DashboardDataDto {
        private int totalRecetas;
        private int recetasPendientes;
        private int recetasCompletadas;
        private int medicamentosActivos;

        public DashboardDataDto() {}

        public int getTotalRecetas() { return totalRecetas; }
        public void setTotalRecetas(int totalRecetas) { this.totalRecetas = totalRecetas; }

        public int getRecetasPendientes() { return recetasPendientes; }
        public void setRecetasPendientes(int recetasPendientes) { this.recetasPendientes = recetasPendientes; }

        public int getRecetasCompletadas() { return recetasCompletadas; }
        public void setRecetasCompletadas(int recetasCompletadas) { this.recetasCompletadas = recetasCompletadas; }

        public int getMedicamentosActivos() { return medicamentosActivos; }
        public void setMedicamentosActivos(int medicamentosActivos) { this.medicamentosActivos = medicamentosActivos; }
    }

    public static class RecetaDataDto {
        private Long id;
        private String fecha;
        private String estado;
        private List<DetalleRecetaDataDto> detalles;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getFecha() { return fecha; }
        public void setFecha(String fecha) { this.fecha = fecha; }

        public String getEstado() { return estado; }
        public void setEstado(String estado) { this.estado = estado; }

        public List<DetalleRecetaDataDto> getDetalles() { return detalles; }
        public void setDetalles(List<DetalleRecetaDataDto> detalles) { this.detalles = detalles; }
    }

    public static class DetalleRecetaDataDto {
        private Long medicamentoId;
        private String medicamentoNombre;
        private int cantidad;

        public Long getMedicamentoId() { return medicamentoId; }
        public void setMedicamentoId(Long medicamentoId) { this.medicamentoId = medicamentoId; }

        public String getMedicamentoNombre() { return medicamentoNombre; }
        public void setMedicamentoNombre(String medicamentoNombre) { this.medicamentoNombre = medicamentoNombre; }

        public int getCantidad() { return cantidad; }
        public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    }
}