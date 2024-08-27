package com.examen.gerden.backend.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Forum {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "forum", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Commentaire> listeDeCommentaires = new HashSet<>();


    private LocalDateTime dateDeCreation;

    private String type;

    @ManyToMany
    @JoinTable(
            name = "forum_utilisateur",
            joinColumns = @JoinColumn(name = "forum_id"),
            inverseJoinColumns = @JoinColumn(name = "utilisateur_id")
    )

    private Set<Utilisateur> utilisateurs = new HashSet<>();

}
