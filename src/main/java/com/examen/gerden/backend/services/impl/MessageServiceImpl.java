package com.examen.gerden.backend.services.impl;

import com.examen.gerden.backend.models.ConversationPrivee;
import com.examen.gerden.backend.models.Message;
import com.examen.gerden.backend.models.Utilisateur;
import com.examen.gerden.backend.repositories.MessageRepository;
import com.examen.gerden.backend.repositories.UtilisateurRepository;
import com.examen.gerden.backend.services.ConversationPriveeService;
import com.examen.gerden.backend.services.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;
    private final ConversationPriveeService conversationPriveeService;
    private final UtilisateurRepository utilisateurRepository;

    @Override
    public Message enregistrerOuModifierMessage(Long conversationId, Long emetteurId, Long recepteurId, String contenu) {
        // Récupérer la conversation
        ConversationPrivee conversation = conversationPriveeService.recupererConversation(conversationId)
                .orElseThrow(() -> new IllegalArgumentException("Conversation non trouvée"));

        // Récupérer l'utilisateur émetteur et récepteur
        Utilisateur emetteur = utilisateurRepository.findById(emetteurId)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur émetteur non trouvé"));
        Utilisateur recepteur = utilisateurRepository.findById(recepteurId)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur récepteur non trouvé"));

        // Créer un nouveau message
        Message message = new Message();
        message.setEmetteur(emetteur);
        message.setRecepteur(recepteur);
        message.setContenu(contenu);
        message.setDateEnvoie(LocalDateTime.now());
        message.setConversation(conversation);

        // Enregistrer le message dans la base de données
        messageRepository.save(message);

        return message;
    }

    @Override
    public Set<Message> getMessagesDeLaConversation(Long conversationId) {
        return conversationPriveeService.getMessagesDeLaConversation(conversationId);
    }

    @Override
    public void supprimerMessage(Long messageId) {
        messageRepository.deleteById(messageId);
    }
}