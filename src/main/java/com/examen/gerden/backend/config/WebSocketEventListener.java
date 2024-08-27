package com.examen.gerden.backend.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {

    private final Map<String, String> activeSessions = new HashMap<>();

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        log.info("Début event de connection");
        String sessionId = (String) event.getMessage().getHeaders().get("simpSessionId");

        // On suppose que l'on a l'ID de l'utilisateur ici
        String userId = (String) event.getUser().getName();

        // Ajouter l'utilisateur à la carte des sessions actives
        addUserSession(sessionId, userId);

        log.info("Nouvelle connexion : " + userId);
        log.info("Nouvelle connexion WebSocket détectée : " + event.getMessage().getHeaders().get("simpSessionId"));
        log.info("Fin event de connection");
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        log.info("Début event de déconnection");
        String sessionId = event.getSessionId();

        log.info("Déconnexion WebSocket détectée : " + sessionId);

        String userId = getUserIdBySessionId(sessionId);

        if (userId != null) {
            removeUserSession(userId);
            log.info("Utilisateur deconnecté : " + userId);
        } else {
            log.warn("Aucun utilisateur trouvé pour l'ID de session" + sessionId);
        }
        removeUserSession(userId);
        log.info("Fin event de déconnection");
    }


    public void endPrivateConversation(String sessionId){
        log.info("Fin de la conversation privée pour la session : " + sessionId);
        removeSession(sessionId);
    }

    public void addUserSession(String sessionId, String userId) {
        activeSessions.put(sessionId, userId);
    }

    public String getUserIdBySessionId(String sessionId) {
        return activeSessions.get(sessionId);
    }

    public void removeSession (String sessionId) {
        activeSessions.remove(sessionId);
    }

    public void removeUserSession(String userId) {
        activeSessions.entrySet().removeIf(entry -> entry.getValue().equals(userId));
    }
}
