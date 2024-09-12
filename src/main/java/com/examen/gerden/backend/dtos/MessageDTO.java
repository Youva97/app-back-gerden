package com.examen.gerden.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageDTO {
    // ID de la conversation à laquelle le message appartient
    private Long conversationId;

    // ID de l'utilisateur qui envoie le message
    private Long emetteurId;

    // ID de l'utilisateur qui reçoit le message
    private Long recepteurId;

    // Contenu du message
    private String contenu;
}