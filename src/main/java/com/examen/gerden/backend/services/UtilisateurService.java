package com.examen.gerden.backend.services;

import com.examen.gerden.backend.models.Role;
import com.examen.gerden.backend.models.Utilisateur;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UtilisateurService {
    boolean validationEmail(Map<String, String> email);

    void enregistrerUtilisateur(Utilisateur utilisateur);

    Utilisateur modifierUtilisateur(Utilisateur utilisateur);


    Utilisateur loadUserByUsername(String username) throws UsernameNotFoundException;

    void modifierMotDePasse(Map<String, String> parametres);

    void nouveauMotDePasse(Map<String, String> parametres);

    List<Utilisateur> recupererTousLesUtilisateurs();

    Optional<Utilisateur> recupererUtilisateur(Long id);

    List<Utilisateur> recupererUtilisateurParRole(Role role);

    void supprimerUtilisateur(Long id);
}
