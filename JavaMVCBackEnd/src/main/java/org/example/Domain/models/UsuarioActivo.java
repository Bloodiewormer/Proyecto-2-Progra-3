package org.example.Domain.models;

public class UsuarioActivo {
    private String id;
    private String nombre;
    private boolean conectado;
    private long ultimaConexion;

    public UsuarioActivo(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.conectado = true;
        this.ultimaConexion = System.currentTimeMillis();
    }

    // Getters y Setters
    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public boolean isConectado() { return conectado; }
    public void setConectado(boolean conectado) { this.conectado = conectado; }
    public long getUltimaConexion() { return ultimaConexion; }
    public void actualizarConexion() { this.ultimaConexion = System.currentTimeMillis(); }
}
