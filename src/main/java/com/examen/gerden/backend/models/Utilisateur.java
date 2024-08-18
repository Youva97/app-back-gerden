package com.examen.gerden.backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.Mac;
import java.util.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Utilisateur implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String prenom;

    private String email;

    private String motDePasse;

    private String telephone;
    private Boolean isActif = false;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "role_id",referencedColumnName = "id")
    private Role role;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "adresse_id", referencedColumnName = "id")
    private Adresse adresse;

    @OneToMany(mappedBy = "emetteur_id", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Message> messagesEnvoyees = new HashSet<>();

    @OneToMany(mappedBy = "recepteur_id", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Message> messageRecues = new HashSet<>();

    @ManyToMany(mappedBy = "utilisateurs")
    private Set<Forum> forums = new HashSet<>();

    @OneToMany(mappedBy = "utilisateur", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Commentaire> commentaires = new HashSet<>();

    @ManyToMany(mappedBy = "participants")
    private Set<ConversationPrivee> conversations = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_"+this.role.getLibelle()));
    }

    @Override
    public String getPassword() { return this.motDePasse; }

    @Override
    public String getUsername() { return this.email; }

    @Override
    public boolean isAccountNonExpired() { return this.isActif; }

    @Override
    public boolean isAccountNonLocked() { return  this.isActif; }

    @Override
    public boolean isCredentialsNonExpired() { return this.isActif; }

    @Override
    public boolean isEnabled() { return this.isActif; }

}
