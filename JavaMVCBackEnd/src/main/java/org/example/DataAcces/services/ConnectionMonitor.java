package org.example.Server;

import org.example.DataAcces.services.UsuarioService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Monitor de conexiones que detecta desconexiones abruptas
 */
public class ConnectionMonitor {
    private final UsuarioService usuarioService;
    private final Map<Long, Long> userLastHeartbeat;
    private final ScheduledExecutorService scheduler;
    private static final long HEARTBEAT_TIMEOUT_MS = 30000; // 30 segundos

    public ConnectionMonitor(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
        this.userLastHeartbeat = new ConcurrentHashMap<>();
        this.scheduler = Executors.newScheduledThreadPool(1);

        startMonitoring();
    }

    public void updateUserHeartbeat(Long userId) {
        userLastHeartbeat.put(userId, System.currentTimeMillis());
    }

    public void registerUser(Long userId) {
        updateUserHeartbeat(userId);
        System.out.println("[ConnectionMonitor] ✅ Usuario registrado: " + userId);
    }

    public void unregisterUser(Long userId) {
        userLastHeartbeat.remove(userId);
        usuarioService.deactivateUser(userId);
        System.out.println("[ConnectionMonitor] 👋 Usuario desregistrado: " + userId);
    }

    private void startMonitoring() {
        scheduler.scheduleAtFixedRate(() -> {
            long currentTime = System.currentTimeMillis();

            userLastHeartbeat.entrySet().removeIf(entry -> {
                Long userId = entry.getKey();
                Long lastHeartbeat = entry.getValue();

                if (currentTime - lastHeartbeat > HEARTBEAT_TIMEOUT_MS) {
                    System.out.println("[ConnectionMonitor] ⚠️ Usuario " + userId +
                            " sin respuesta. Desactivando...");
                    try {
                        usuarioService.deactivateUser(userId);
                    } catch (Exception e) {
                        System.err.println("[ConnectionMonitor] ❌ Error: " + e.getMessage());
                    }
                    return true;
                }
                return false;
            });
        }, 10, 10, TimeUnit.SECONDS);
    }

    public void shutdown() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
            System.out.println("[ConnectionMonitor] 🛑 Monitor detenido");
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
        }
    }
}