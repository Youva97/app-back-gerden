package com.examen.gerden.backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String contenu;


    private LocalDateTime dateEnvoie;

    private String urlPhoto;

    @ManyToOne
    @JoinColumn(name = "recepteur_id", nullable = false)
    private Utilisateur recepteur;

    @ManyToOne
    @JoinColumn(name = "emetteur_id", nullable = false)
    private  Utilisateur emetteur;

    @ManyToOne
    @JoinColumn(name = "conversation_id", nullable = false)
    private ConversationPrivee conversation;
}
