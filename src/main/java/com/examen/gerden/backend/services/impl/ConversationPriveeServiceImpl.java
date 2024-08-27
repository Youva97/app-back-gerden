package com.examen.gerden.backend.services.impl;

import com.examen.gerden.backend.models.ConversationPrivee;
import com.examen.gerden.backend.models.Message;
import com.examen.gerden.backend.models.Utilisateur;
import com.examen.gerden.backend.repositories.ConversationPriveeRepository;
import com.examen.gerden.backend.repositories.MessageRepository;
import com.examen.gerden.backend.repositories.UtilisateurRepository;
import com.examen.gerden.backend.services.ConversationPriveeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ConversationPriveeServiceImpl implements ConversationPriveeService {
    private final ConversationPriveeRepository conversationPriveeRepository;

    private final UtilisateurRepository utilisateurRepository;

    private final MessageRepository messageRepository;

    @Override
    public ConversationPrivee creerConversation(Message message) {

        ConversationPrivee conversation = new ConversationPrivee();
        conversation.getListeMessages().add(message);
        return conversationPriveeRepository.save(conversation);

    }

    @Override
    public Message ajouterOuModifierMessage(Long conversationId, Long idEmetteur, Long idRecepteur, Message message) {
        // Récupérer la conversation privée
        ConversationPrivee conversation = conversationPriveeRepository.findById(conversationId)
                .orElseThrow(() -> new IllegalArgumentException("Conversation non trouvée"));

        // Récupérer l'utilisateur émetteur
        Utilisateur emetteur = utilisateurRepository.findById(idEmetteur)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur émetteur non trouvé"));
        // Récupérer l'utilisateur récepteur
        Utilisateur recepteur = utilisateurRepository.findById(idRecepteur)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur émetteur non trouvé"));

        // Associer l'utilisateur émetteur au message
        message.setEmetteur(emetteur);
        message.setRecepteur(recepteur);
        message.setConversation(conversation);

        // Ajouter le message à la conversation privée
        conversation.getListeMessages().add(message);

        // Enregistrer la conversation privée mise à jour avec le nouveau message
        conversationPriveeRepository.save(conversation);

        return messageRepository.save(message);
    }


    @Override
    public Set<Message> getMessagesDeLaConversation(Long conversationId) {
        Optional<ConversationPrivee> optionalConversation = conversationPriveeRepository.findById(conversationId);
        if (optionalConversation.isPresent()) {
            return optionalConversation.get().getListeMessages();
        }
        throw new IllegalArgumentException("Conversation non trouvée");
    }

    @Override
    public Set<ConversationPrivee> listerConversationsUtilisateur(Long utilisateurId) {
        Set<Message> messages = messageRepository.findByRecepteurIdOrEmetteurId(utilisateurId, utilisateurId);
        Set<ConversationPrivee> conversations = new HashSet<>();
        for (Message message : messages) {
            conversations.add(message.getConversation());
        }
        return conversations;
    }

    @Override
    public void supprimerMessage(Long messageId) {
        messageRepository.deleteById(messageId);
    }

    @Override
    public void supprimerConversation(Long conversationId) {
        conversationPriveeRepository.deleteById(conversationId);
    }
}
