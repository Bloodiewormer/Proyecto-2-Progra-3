package org.example.DataAcces.services;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Servicio para obtener datos del dashboard
 */
public class DashboardService {
    private final SessionFactory sessionFactory;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public DashboardService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * Obtiene datos agregados para el dashboard
     */
    public DashboardData getDashboardData(String startDate, String endDate) {
        try (Session session = sessionFactory.openSession()) {
            // Parsear fechas
            LocalDate inicio = LocalDate.parse(startDate, DATE_FORMATTER);
            LocalDate fin = LocalDate.parse(endDate, DATE_FORMATTER);

            // Total de recetas en el rango
            Long totalRecetas = (Long) session.createQuery(
                            "SELECT COUNT(r) FROM Receta r WHERE r.fechaConfeccion BETWEEN :start AND :end")
                    .setParameter("start", inicio.atStartOfDay())
                    .setParameter("end", fin.atTime(23, 59, 59))
                    .uniqueResult();

            // Recetas pendientes
            Long recetasPendientes = (Long) session.createQuery(
                            "SELECT COUNT(r) FROM Receta r WHERE r.estado = 'CONFECCIONADA' AND r.fechaConfeccion BETWEEN :start AND :end")
                    .setParameter("start", inicio.atStartOfDay())
                    .setParameter("end", fin.atTime(23, 59, 59))
                    .uniqueResult();

            // Recetas completadas
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
            // Retornar datos vacíos en caso de error
            return new DashboardData(0, 0, 0, 0);
        }
    }

    /**
     * DTO interno para la respuesta del dashboard
     */
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

        @Override
        public String toString() {
            return "DashboardData{" +
                    "totalRecetas=" + totalRecetas +
                    ", recetasPendientes=" + recetasPendientes +
                    ", recetasCompletadas=" + recetasCompletadas +
                    ", medicamentosActivos=" + medicamentosActivos +
                    '}';
        }
    }
}