package com.examen.gerden.backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Jwt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean estDesactive;
    private boolean estExpire;
    private String valeur;
    @OneToOne(cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    @JoinColumn(name = "refresh_token_id")
    private RefreshToken refreshToken;
    @ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE})
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;
}
