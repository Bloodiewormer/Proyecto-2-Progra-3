package org.example.DataAcces.services;

import org.example.Domain.models.Mensaje;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Servicio para gestionar mensajes entre usuarios
 */
public class MensajeService {
    private final SessionFactory sessionFactory;

    public MensajeService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * Enviar un mensaje de un usuario a otro
     */
    public Mensaje enviarMensaje(Long senderId, Long recipientId, String contenido) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();

            Mensaje mensaje = new Mensaje();
            mensaje.setSenderId(senderId);
            mensaje.setRecipientId(recipientId);
            mensaje.setMessage(contenido);
            mensaje.setStatus("SENT");
            mensaje.setCreatedAt(LocalDateTime.now());

            session.persist(mensaje);
            tx.commit();

            System.out.println("[MensajeService] ✅ Mensaje enviado de " + senderId + " a " + recipientId);
            return mensaje;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.err.println("[MensajeService] ❌ Error enviando mensaje: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Obtener historial de mensajes entre dos usuarios
     */
    public List<Mensaje> obtenerHistorial(Long user1Id, Long user2Id) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "FROM Mensaje m WHERE " +
                    "(m.senderId = :user1 AND m.recipientId = :user2) OR " +
                    "(m.senderId = :user2 AND m.recipientId = :user1) " +
                    "ORDER BY m.createdAt ASC";

            Query<Mensaje> query = session.createQuery(hql, Mensaje.class);
            query.setParameter("user1", user1Id);
            query.setParameter("user2", user2Id);

            List<Mensaje> mensajes = query.list();
            System.out.println("[MensajeService] 📜 Historial recuperado: " + mensajes.size() + " mensajes");
            return mensajes;
        } catch (Exception e) {
            System.err.println("[MensajeService] ❌ Error obteniendo historial: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Marcar mensaje como leído
     */
    public void marcarComoLeido(Long mensajeId) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();

            Mensaje mensaje = session.find(Mensaje.class, mensajeId);
            if (mensaje != null) {
                mensaje.setReadAt(LocalDateTime.now());
                mensaje.setStatus("READ");
                session.merge(mensaje);
            }

            tx.commit();
            System.out.println("[MensajeService] ✅ Mensaje marcado como leído: " + mensajeId);
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.err.println("[MensajeService] ❌ Error marcando como leído: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Obtener mensajes no leídos para un usuario
     */
    public List<Mensaje> obtenerMensajesNoLeidos(Long userId) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "FROM Mensaje m WHERE m.recipientId = :userId AND m.readAt IS NULL " +
                    "ORDER BY m.createdAt DESC";

            Query<Mensaje> query = session.createQuery(hql, Mensaje.class);
            query.setParameter("userId", userId);

            return query.list();
        } catch (Exception e) {
            System.err.println("[MensajeService] ❌ Error obteniendo no leídos: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Obtener todos los mensajes para un usuario (enviados y recibidos)
     */
    public List<Mensaje> obtenerMensajesUsuario(Long userId) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "FROM Mensaje m WHERE m.senderId = :userId OR m.recipientId = :userId " +
                    "ORDER BY m.createdAt DESC";

            Query<Mensaje> query = session.createQuery(hql, Mensaje.class);
            query.setParameter("userId", userId);

            return query.list();
        } catch (Exception e) {
            System.err.println("[MensajeService] ❌ Error obteniendo mensajes: " + e.getMessage());
            throw e;
        }
    }
}