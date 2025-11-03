package org.example.DataAcces.services;

import org.example.Domain.models.*;
import org.example.Utilities.EstadoReceta;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Servicio para gestionar recetas médicas
 */
public class RecetaService {
    private final SessionFactory sessionFactory;

    public RecetaService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * Crea una nueva receta
     */
    public Receta create(Long pacienteId, Long medicoId, LocalDateTime fechaRetiro) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();

            Paciente paciente = session.find(Paciente.class, pacienteId);
            Medico medico = session.find(Medico.class, medicoId);

            if (paciente == null || medico == null) {
                tx.rollback();
                System.err.println("[RecetaService] Paciente o médico no encontrado");
                return null;
            }

            Receta receta = new Receta();
            receta.setPaciente(paciente);
            receta.setMedico(medico);
            receta.setFechaRetiro(fechaRetiro);
            receta.setEstado(EstadoReceta.PROCESO);

            session.persist(receta);
            tx.commit();

            System.out.println("[RecetaService] Receta creada: ID=" + receta.getId());
            return receta;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.err.println("[RecetaService] Error creando receta: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Obtiene una receta por ID con todos sus detalles
     */
    public Receta getById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            Receta receta = session.find(Receta.class, id);
            if (receta != null) {
                // Inicializar colecciones lazy
                Hibernate.initialize(receta.getDetalles());
                Hibernate.initialize(receta.getPaciente());
                Hibernate.initialize(receta.getMedico());
            }
            return receta;
        } catch (Exception e) {
            System.err.println("[RecetaService] Error obteniendo receta: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Obtiene todas las recetas
     */
    public List<Receta> getAll() {
        try (Session session = sessionFactory.openSession()) {
            List<Receta> recetas = session.createQuery("FROM Receta", Receta.class).list();

            // Inicializar colecciones lazy para cada receta
            recetas.forEach(r -> {
                Hibernate.initialize(r.getDetalles());
                Hibernate.initialize(r.getPaciente());
                Hibernate.initialize(r.getMedico());
            });

            return recetas;
        } catch (Exception e) {
            System.err.println("[RecetaService] Error obteniendo recetas: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Obtiene recetas por paciente
     */
    public List<Receta> getByPaciente(Long pacienteId) {
        try (Session session = sessionFactory.openSession()) {
            List<Receta> recetas = session.createQuery(
                            "FROM Receta WHERE paciente.id = :pacienteId ORDER BY fechaConfeccion DESC",
                            Receta.class
                    )
                    .setParameter("pacienteId", pacienteId)
                    .list();

            // Inicializar colecciones lazy
            recetas.forEach(r -> {
                Hibernate.initialize(r.getDetalles());
                Hibernate.initialize(r.getMedico());
            });

            return recetas;
        } catch (Exception e) {
            System.err.println("[RecetaService] Error obteniendo recetas por paciente: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Obtiene recetas por médico
     */
    public List<Receta> getByMedico(Long medicoId) {
        try (Session session = sessionFactory.openSession()) {
            List<Receta> recetas = session.createQuery(
                            "FROM Receta WHERE medico.id = :medicoId ORDER BY fechaConfeccion DESC",
                            Receta.class
                    )
                    .setParameter("medicoId", medicoId)
                    .list();

            // Inicializar colecciones lazy
            recetas.forEach(r -> {
                Hibernate.initialize(r.getDetalles());
                Hibernate.initialize(r.getPaciente());
            });

            return recetas;
        } catch (Exception e) {
            System.err.println("[RecetaService] Error obteniendo recetas por médico: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Obtiene recetas por estado
     */
    public List<Receta> getByEstado(EstadoReceta estado) {
        try (Session session = sessionFactory.openSession()) {
            List<Receta> recetas = session.createQuery(
                            "FROM Receta WHERE estado = :estado ORDER BY fechaConfeccion DESC",
                            Receta.class
                    )
                    .setParameter("estado", estado)
                    .list();

            // Inicializar colecciones lazy
            recetas.forEach(r -> {
                Hibernate.initialize(r.getDetalles());
                Hibernate.initialize(r.getPaciente());
                Hibernate.initialize(r.getMedico());
            });

            return recetas;
        } catch (Exception e) {
            System.err.println("[RecetaService] Error obteniendo recetas por estado: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Actualiza el estado de una receta
     */
    public Receta updateEstado(Long id, EstadoReceta nuevoEstado) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();

            Receta receta = session.find(Receta.class, id);
            if (receta != null) {
                receta.setEstado(nuevoEstado);
                session.merge(receta);

                // IMPORTANTE: inicializar relaciones/colecciones necesarias antes de cerrar la sesión
                Hibernate.initialize(receta.getDetalles());
                Hibernate.initialize(receta.getPaciente());
                Hibernate.initialize(receta.getMedico());

                System.out.println("[RecetaService] Estado actualizado - Receta ID=" + id + ", Estado=" + nuevoEstado);
            }

            tx.commit();
            return receta;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.err.println("[RecetaService] Error actualizando estado: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Agrega un detalle a una receta existente
     */
    public void addDetalle(Long recetaId, Long medicamentoId, int cantidad, String indicaciones, int dias) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();

            Receta receta = session.find(Receta.class, recetaId);
            Medicamento medicamento = session.find(Medicamento.class, medicamentoId);

            if (receta != null && medicamento != null) {
                DetalleReceta detalle = new DetalleReceta(medicamento, cantidad, indicaciones, dias);
                receta.addDetalle(detalle);
                session.merge(receta);
                System.out.println("[RecetaService] Detalle agregado a receta ID=" + recetaId);
            } else {
                System.err.println("[RecetaService] Receta o medicamento no encontrado");
            }

            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.err.println("[RecetaService] Error agregando detalle: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Elimina una receta
     */
    public boolean delete(Long id) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();

            Receta receta = session.find(Receta.class, id);
            if (receta != null) {
                session.remove(receta);
                tx.commit();
                System.out.println("[RecetaService] Receta eliminada: ID=" + id);
                return true;
            }

            tx.rollback();
            return false;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.err.println("[RecetaService] Error eliminando receta: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Obtiene recetas en un rango de fechas
     */
    public List<Receta> getByFechaRange(LocalDateTime inicio, LocalDateTime fin) {
        try (Session session = sessionFactory.openSession()) {
            List<Receta> recetas = session.createQuery(
                            "FROM Receta WHERE fechaConfeccion BETWEEN :inicio AND :fin ORDER BY fechaConfeccion DESC",
                            Receta.class
                    )
                    .setParameter("inicio", inicio)
                    .setParameter("fin", fin)
                    .list();

            // Inicializar colecciones lazy
            recetas.forEach(r -> {
                Hibernate.initialize(r.getDetalles());
                Hibernate.initialize(r.getPaciente());
                Hibernate.initialize(r.getMedico());
            });

            return recetas;
        } catch (Exception e) {
            System.err.println("[RecetaService] Error obteniendo recetas por rango de fecha: " + e.getMessage());
            throw e;
        }
    }
}