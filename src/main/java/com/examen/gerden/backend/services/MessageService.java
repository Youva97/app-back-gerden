package com.examen.gerden.backend.services;

import com.examen.gerden.backend.models.Message;

import java.util.Set;

public interface MessageService {
    Message enregistrerOuModifierMessage(Long conversationId, Long emetteurId, Long recepteurId, String contenu);

    Set<Message> getMessagesDeLaConversation(Long conversationId);

    void supprimerMessage(Long messageId);
}
