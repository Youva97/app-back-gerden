package com.examen.gerden.backend.services;

import com.examen.gerden.backend.models.ConversationPrivee;
import com.examen.gerden.backend.models.Message;

import java.util.Optional;
import java.util.Set;

public interface ConversationPriveeService {
    ConversationPrivee creerConversation(Message message);

    Message ajouterOuModifierMessage(Long conversationId, Long idEmetteur, Long idRecepteur, Message message);

    Set<Message> getMessagesDeLaConversation(Long conversationId);

    Optional<ConversationPrivee> recupererConversation(Long conversationId);

    Set<ConversationPrivee> listerConversationsUtilisateur(Long utilisateurId);

    void supprimerMessage(Long messageId);

    void supprimerConversation(Long conversationId);
}
