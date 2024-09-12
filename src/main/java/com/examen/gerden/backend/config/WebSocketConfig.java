package com.examen.gerden.backend.config;

import com.examen.gerden.backend.services.impl.JwtServiceImpl;
import com.examen.gerden.backend.services.impl.UtilisateurServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private static final Logger log = LoggerFactory.getLogger(WebSocketConfig.class);
    private final JwtServiceImpl jwtService;
    private  final UtilisateurServiceImpl utilisateurService;

    public WebSocketConfig(JwtServiceImpl jwtService, UtilisateurServiceImpl utilisateurService){
        this.jwtService = jwtService;
        this.utilisateurService = utilisateurService;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        log.info("Début conf broker");
        // Configure un broker simple qui permet de gérer les destinations "/topic" et "/queue"
        registry.enableSimpleBroker("/topic");
        // Définit le préfixe pour les destinations d'application
        registry.setApplicationDestinationPrefixes("/ws/");
        // Définit l'utilisateur à qui on envoie un message
        // registry.setUserDestinationPrefix("/utilisateur");
        log.info("Fin conf broker");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat-websocket").setAllowedOrigins("*");
        registry.addEndpoint("/chat-websocket").setAllowedOrigins("/http://localhost:4200").withSockJS();
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    String token = accessor.getFirstNativeHeader("Authorization");
                    if (token != null && token.startsWith("Bearer ")) {
                        token = token.substring(7);
                        if (!jwtService.isTokenExpired(token)) {
                            String username = jwtService.extractUsername(token);
                            UserDetails userDetails = utilisateurService.loadUserByUsername(username);
                            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                            accessor.setUser(authenticationToken);
                        } else {
                            throw new IllegalArgumentException("Invalid Token");
                        }
                    }
                }
                return message;
            }
        });
    }

}
