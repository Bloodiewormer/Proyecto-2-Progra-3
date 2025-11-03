package org.example.DataAcces;

import org.example.Domain.models.*;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import java.io.IOException;
import java.util.Properties;

/**
 * Utilidad para gestionar la SessionFactory de Hibernate.
 * Configura todas las entidades del sistema médico.
 */
public class HibernateUtil {

    private static final SessionFactory sessionFactory;

    static {
        try {
            System.out.println("[HibernateUtil] Iniciando configuración de Hibernate...");

            // Cargar propiedades de hibernate.properties
            Properties properties = new Properties();
            properties.load(HibernateUtil.class.getClassLoader().getResourceAsStream("hibernate.properties"));

            System.out.println("[HibernateUtil] Propiedades cargadas, construyendo SessionFactory...");

            // Crear SessionFactory con todas las entidades del sistema médico
            sessionFactory = new Configuration()
                    .addProperties(properties)
                    // Entidades del sistema de usuarios
                    .addAnnotatedClass(Usuario.class)
                    .addAnnotatedClass(Administrador.class)
                    .addAnnotatedClass(Medico.class)
                    .addAnnotatedClass(Farmaceuta.class)
                    // Entidades del sistema médico
                    .addAnnotatedClass(Paciente.class)
                    .addAnnotatedClass(Medicamento.class)
                    .addAnnotatedClass(Receta.class)
                    .addAnnotatedClass(DetalleReceta.class)
                    .addAnnotatedClass(Mensaje.class)
                    .buildSessionFactory();

            System.out.println("[HibernateUtil] SessionFactory creado exitosamente");

        } catch (IOException e) {
            System.err.println("[HibernateUtil] Error cargando hibernate.properties: " + e.getMessage());
            throw new ExceptionInInitializerError("No se pudo cargar hibernate.properties: " + e.getMessage());
        } catch (Throwable ex) {
            System.err.println("[HibernateUtil] Error creando SessionFactory: " + ex.getMessage());
            ex.printStackTrace();
            throw new ExceptionInInitializerError("Fallo en la creación inicial de SessionFactory: " + ex);
        }
    }

    /**
     * Obtiene la SessionFactory única del sistema
     */
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /**
     * Cierra la SessionFactory al finalizar la aplicación
     */
    public static void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            System.out.println("[HibernateUtil] Cerrando SessionFactory...");
            sessionFactory.close();
            System.out.println("[HibernateUtil] SessionFactory cerrado");
        }
    }
}