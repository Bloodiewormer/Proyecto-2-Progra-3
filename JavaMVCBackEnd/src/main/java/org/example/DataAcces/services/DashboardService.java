package org.example.DataAcces.services;

import org.example.Domain.models.Receta;
import org.example.Domain.models.DetalleReceta;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Servicio para obtener datos del Dashboard
 */
public class DashboardService {
    private final SessionFactory sessionFactory;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter YEAR_MONTH_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");

    public DashboardService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * Obtiene datos básicos agregados para el dashboard
     */
    public DashboardData getDashboardData(String startDate, String endDate) {
        try (Session session = sessionFactory.openSession()) {
            LocalDate inicio = LocalDate.parse(startDate, DATE_FORMATTER);
            LocalDate fin = LocalDate.parse(endDate, DATE_FORMATTER);

            // Total de recetas en el rango
            Long totalRecetas = (Long) session.createQuery(
                            "SELECT COUNT(r) FROM Receta r WHERE r.fechaConfeccion BETWEEN :start AND :end")
                    .setParameter("start", inicio.atStartOfDay())
                    .setParameter("end", fin.atTime(23, 59, 59))
                    .uniqueResult();

            // Recetas pendientes (CONFECCIONADA)
            Long recetasPendientes = (Long) session.createQuery(
                            "SELECT COUNT(r) FROM Receta r WHERE r.estado = 'CONFECCIONADA' AND r.fechaConfeccion BETWEEN :start AND :end")
                    .setParameter("start", inicio.atStartOfDay())
                    .setParameter("end", fin.atTime(23, 59, 59))
                    .uniqueResult();

            // Recetas completadas (ENTREGADA)
            Long recetasCompletadas = (Long) session.createQuery(
                            "SELECT COUNT(r) FROM Receta r WHERE r.estado = 'ENTREGADA' AND r.fechaConfeccion BETWEEN :start AND :end")
                    .setParameter("start", inicio.atStartOfDay())
                    .setParameter("end", fin.atTime(23, 59, 59))
                    .uniqueResult();

            // Medicamentos totales
            Long medicamentosActivos = (Long) session.createQuery(
                            "SELECT COUNT(m) FROM Medicamento m")
                    .uniqueResult();

            System.out.println("[DashboardService] Datos obtenidos - Total: " + totalRecetas +
                    ", Pendientes: " + recetasPendientes +
                    ", Completadas: " + recetasCompletadas);

            return new DashboardData(
                    totalRecetas != null ? totalRecetas.intValue() : 0,
                    recetasPendientes != null ? recetasPendientes.intValue() : 0,
                    recetasCompletadas != null ? recetasCompletadas.intValue() : 0,
                    medicamentosActivos != null ? medicamentosActivos.intValue() : 0
            );
        } catch (Exception e) {
            System.err.println("[DashboardService] Error obteniendo datos: " + e.getMessage());
            e.printStackTrace();
            return new DashboardData(0, 0, 0, 0);
        }
    }

    /**
     * Obtiene recetas dentro del rango de fechas especificado
     */
    public List<RecetaData> getRecetasInRange(String startDate, String endDate) {
        try (Session session = sessionFactory.openSession()) {
            LocalDate inicio = LocalDate.parse(startDate, DATE_FORMATTER);
            LocalDate fin = LocalDate.parse(endDate, DATE_FORMATTER);

            System.out.println("[DashboardService] Buscando recetas...");
            System.out.println("  Fecha inicio: " + inicio.atStartOfDay());
            System.out.println("  Fecha fin: " + fin.atTime(23, 59, 59));

            List<Receta> recetas = session.createQuery(
                            "SELECT DISTINCT r FROM Receta r " +
                                    "LEFT JOIN FETCH r.detalles d " +
                                    "LEFT JOIN FETCH d.medicamento " +
                                    "WHERE r.fechaConfeccion BETWEEN :start AND :end",
                            Receta.class)
                    .setParameter("start", inicio.atStartOfDay())
                    .setParameter("end", fin.atTime(23, 59, 59))
                    .getResultList();

            System.out.println("[DashboardService] Recetas obtenidas: " + recetas.size());

            // Log de cada receta encontrada
            for (Receta r : recetas) {
                System.out.println("  - ID: " + r.getId() + ", Fecha: " + r.getFechaConfeccion() + ", Estado: " + r.getEstado());
            }

            return recetas.stream()
                    .map(this::convertToRecetaData)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("[DashboardService] Error obteniendo recetas: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Obtiene unidades de medicamentos prescritas por mes
     * Retorna: Map<"medicamentoId", Map<"yyyy-MM", cantidad>>
     */
    public Map<String, Map<String, Integer>> getMedicamentosUnidadesPorMes(
            String startDate,
            String endDate,
            List<Long> medicamentoIds) {

        try (Session session = sessionFactory.openSession()) {
            LocalDate inicio = LocalDate.parse(startDate, DATE_FORMATTER);
            LocalDate fin = LocalDate.parse(endDate, DATE_FORMATTER);

            System.out.println("[DashboardService] Buscando medicamentos por mes...");
            System.out.println("  Fecha inicio: " + inicio.atStartOfDay());
            System.out.println("  Fecha fin: " + fin.atTime(23, 59, 59));
            System.out.println("  Medicamentos solicitados: " + medicamentoIds);

            // Obtener todas las recetas con sus detalles
            List<Receta> recetas = session.createQuery(
                            "SELECT DISTINCT r FROM Receta r " +
                                    "LEFT JOIN FETCH r.detalles d " +
                                    "LEFT JOIN FETCH d.medicamento " +
                                    "WHERE r.fechaConfeccion BETWEEN :start AND :end",
                            Receta.class)
                    .setParameter("start", inicio.atStartOfDay())
                    .setParameter("end", fin.atTime(23, 59, 59))
                    .getResultList();

            System.out.println("[DashboardService] Recetas encontradas para medicamentos: " + recetas.size());

            Map<String, Map<String, Integer>> resultado = new HashMap<>();

            // Inicializar el mapa para cada medicamento
            for (Long medId : medicamentoIds) {
                resultado.put(String.valueOf(medId), new HashMap<>());
            }

            // Procesar cada receta
            for (Receta receta : recetas) {
                YearMonth ym = YearMonth.from(receta.getFechaConfeccion().toLocalDate());
                String yearMonth = ym.format(YEAR_MONTH_FORMATTER);

                System.out.println("  Procesando receta ID: " + receta.getId() + ", Fecha: " + receta.getFechaConfeccion() + ", Mes: " + yearMonth);

                if (receta.getDetalles() != null) {
                    System.out.println("    Detalles: " + receta.getDetalles().size());
                    for (DetalleReceta detalle : receta.getDetalles()) {
                        if (detalle.getMedicamento() != null) {
                            Long medId = detalle.getMedicamento().getId();
                            String medIdStr = String.valueOf(medId);

                            System.out.println("      Medicamento ID: " + medId + ", Cantidad: " + detalle.getCantidad());

                            if (resultado.containsKey(medIdStr)) {
                                Map<String, Integer> porMes = resultado.get(medIdStr);
                                int cantidadActual = porMes.getOrDefault(yearMonth, 0);
                                porMes.put(yearMonth, cantidadActual + detalle.getCantidad());
                                System.out.println("        Acumulado en " + yearMonth + ": " + (cantidadActual + detalle.getCantidad()));
                            }
                        }
                    }
                }
            }

            System.out.println("[DashboardService] Unidades por mes calculadas: " + resultado);
            return resultado;

        } catch (Exception e) {
            System.err.println("[DashboardService] Error calculando unidades por mes: " + e.getMessage());
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    /**
     * Obtiene la distribución de recetas por estado
     */
    public Map<String, Integer> getRecetasPorEstado(String startDate, String endDate) {
        try (Session session = sessionFactory.openSession()) {
            LocalDate inicio = LocalDate.parse(startDate, DATE_FORMATTER);
            LocalDate fin = LocalDate.parse(endDate, DATE_FORMATTER);

            List<Object[]> resultados = session.createQuery(
                            "SELECT r.estado, COUNT(r) FROM Receta r " +
                                    "WHERE r.fechaConfeccion BETWEEN :start AND :end " +
                                    "GROUP BY r.estado",
                            Object[].class)
                    .setParameter("start", inicio.atStartOfDay())
                    .setParameter("end", fin.atTime(23, 59, 59))
                    .getResultList();

            Map<String, Integer> distribucion = new HashMap<>();
            for (Object[] resultado : resultados) {
                String estado = resultado[0].toString();
                Long cantidad = (Long) resultado[1];
                distribucion.put(estado, cantidad.intValue());
            }

            System.out.println("[DashboardService] Distribución por estado: " + distribucion);
            return distribucion;

        } catch (Exception e) {
            System.err.println("[DashboardService] Error obteniendo distribución por estado: " + e.getMessage());
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    /**
     * Convierte una entidad Receta a RecetaData (DTO)
     */
    private RecetaData convertToRecetaData(Receta receta) {
        List<DetalleRecetaData> detalles = new ArrayList<>();
        if (receta.getDetalles() != null) {
            detalles = receta.getDetalles().stream()
                    .map(d -> new DetalleRecetaData(
                            d.getMedicamento() != null ? d.getMedicamento().getId() : null,
                            d.getMedicamento() != null ? d.getMedicamento().getNombre() : "",
                            d.getCantidad()
                    ))
                    .collect(Collectors.toList());
        }

        return new RecetaData(
                receta.getId(),
                receta.getFechaConfeccion().toString(),
                receta.getEstado().toString(),
                detalles
        );
    }

    // =============== DTOs internos ===============

    public static class DashboardData {
        private final int totalRecetas;
        private final int recetasPendientes;
        private final int recetasCompletadas;
        private final int medicamentosActivos;

        public DashboardData(int totalRecetas, int recetasPendientes, int recetasCompletadas, int medicamentosActivos) {
            this.totalRecetas = totalRecetas;
            this.recetasPendientes = recetasPendientes;
            this.recetasCompletadas = recetasCompletadas;
            this.medicamentosActivos = medicamentosActivos;
        }

        public int getTotalRecetas() { return totalRecetas; }
        public int getRecetasPendientes() { return recetasPendientes; }
        public int getRecetasCompletadas() { return recetasCompletadas; }
        public int getMedicamentosActivos() { return medicamentosActivos; }
    }

    public static class RecetaData {
        private final Long id;
        private final String fecha;
        private final String estado;
        private final List<DetalleRecetaData> detalles;

        public RecetaData(Long id, String fecha, String estado, List<DetalleRecetaData> detalles) {
            this.id = id;
            this.fecha = fecha;
            this.estado = estado;
            this.detalles = detalles;
        }

        public Long getId() { return id; }
        public String getFecha() { return fecha; }
        public String getEstado() { return estado; }
        public List<DetalleRecetaData> getDetalles() { return detalles; }
    }

    public static class DetalleRecetaData {
        private final Long medicamentoId;
        private final String medicamentoNombre;
        private final int cantidad;

        public DetalleRecetaData(Long medicamentoId, String medicamentoNombre, int cantidad) {
            this.medicamentoId = medicamentoId;
            this.medicamentoNombre = medicamentoNombre;
            this.cantidad = cantidad;
        }

        public Long getMedicamentoId() { return medicamentoId; }
        public String getMedicamentoNombre() { return medicamentoNombre; }
        public int getCantidad() { return cantidad; }
    }
}