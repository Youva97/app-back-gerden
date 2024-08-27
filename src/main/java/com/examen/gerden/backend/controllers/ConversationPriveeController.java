package com.examen.gerden.backend.controllers;

import com.examen.gerden.backend.models.ConversationPrivee;
import com.examen.gerden.backend.models.Message;
import com.examen.gerden.backend.services.ConversationPriveeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/conversations")
public class ConversationPriveeController {
    private final ConversationPriveeService conversationPriveeService;

    @Autowired
    public ConversationPriveeController(ConversationPriveeService conversationPriveeService) {
        this.conversationPriveeService = conversationPriveeService;
    }

    @PostMapping("/creer")
    public ResponseEntity<ConversationPrivee> creerConversation(@RequestParam Message message) {
        try {
            ConversationPrivee conversation = conversationPriveeService.creerConversation(message);
            return new ResponseEntity<>(conversation, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{conversationId}/{idEmetteur}/{idRecepteur}/messages")
    public ResponseEntity<Message> ajouterOuModifierMessage(Long conversationId,
                                                            Long emetteurId,
                                                            Long recepteurId,
                                                            @RequestBody Message message) {
        try {
            Message messageEnvoye =
                    conversationPriveeService.ajouterOuModifierMessage(conversationId, emetteurId, recepteurId, message);
            // Retourner le message ajouté
            return new ResponseEntity<>(messageEnvoye, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/{conversationId}/messages")
    public ResponseEntity<Set<Message>> getMessagesDeLaConversation(@PathVariable Long conversationId) {
        try {
            Set<Message> messages = conversationPriveeService.getMessagesDeLaConversation(conversationId);
            return new ResponseEntity<>(messages, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/utilisateur/{utilisateurId}")
    public ResponseEntity<Set<ConversationPrivee>> listerConversationsUtilisateur(@PathVariable Long utilisateurId) {
        Set<ConversationPrivee> conversations = conversationPriveeService.listerConversationsUtilisateur(utilisateurId);
        return new ResponseEntity<>(conversations, HttpStatus.OK);
    }

    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<Void> supprimerMessage(@PathVariable Long messageId) {
        conversationPriveeService.supprimerMessage(messageId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{conversationId}")
    public ResponseEntity<Void> supprimerConversation(@PathVariable Long conversationId) {
        conversationPriveeService.supprimerConversation(conversationId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
