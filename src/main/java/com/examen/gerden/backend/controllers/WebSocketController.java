package com.examen.gerden.backend.controllers;

import com.examen.gerden.backend.dtos.MessageDTO;
import com.examen.gerden.backend.models.Message;
import com.examen.gerden.backend.services.ConversationPriveeService;
import com.examen.gerden.backend.services.MessageService;
import com.examen.gerden.backend.services.UtilisateurService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class WebSocketController {

    private final MessageService messageService;
    private final ConversationPriveeService conversationPriveeService;
    private final UtilisateurService utilisateurService;

    @Autowired
    public WebSocketController(MessageService messageService,
                               ConversationPriveeService conversationPriveeService,
                               UtilisateurService utilisateurService) {
        this.messageService = messageService;
        this.conversationPriveeService = conversationPriveeService;
        this.utilisateurService = utilisateurService;
    }

    // Mapping pour l'envoi d'un message dans une conversation privée
    @MessageMapping("/chat/{conversationId}")
    @SendTo("/topic/{conversationId}")
    public MessageDTO chat(@DestinationVariable Long conversationId, MessageDTO messageDTO) {
        log.info("Message reçu dans la conversation: " + conversationId);

        // Enregistrer ou modifier le message dans la conversation
        Message message = messageService.enregistrerOuModifierMessage(
                conversationId,
                messageDTO.getEmetteurId(),
                messageDTO.getRecepteurId(),
                messageDTO.getContenu()
        );

        // Retourner le message enregistré au format DTO pour l'envoyer aux autres participants
        return new MessageDTO(
                message.getConversation().getId(),
                message.getEmetteur().getId(),
                message.getRecepteur().getId(),
                message.getContenu()
        );
    }

    // Optionnel : Méthode pour récupérer tous les messages d'une conversation
    @MessageMapping("/chat/messages/{conversationId}")
    @SendTo("/topic/messages/{conversationId}")
    public Set<MessageDTO> getMessagesDeLaConversation(@DestinationVariable Long conversationId) {
        Set<Message> messages = conversationPriveeService.getMessagesDeLaConversation(conversationId);
        return messages.stream()
                .map(message -> new MessageDTO(
                        message.getConversation().getId(),
                        message.getEmetteur().getId(),
                        message.getRecepteur().getId(),
                        message.getContenu()
                ))
                .collect(Collectors.toSet());
    }
}