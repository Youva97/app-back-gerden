package com.examen.gerden.backend.services.impl;

import com.examen.gerden.backend.exceptions.ResourceAlreadyExistException;
import com.examen.gerden.backend.models.Adresse;
import com.examen.gerden.backend.models.Role;
import com.examen.gerden.backend.models.Utilisateur;
import com.examen.gerden.backend.repositories.AdresseRepository;
import com.examen.gerden.backend.repositories.RoleRepository;
import com.examen.gerden.backend.repositories.UtilisateurRepository;
import com.examen.gerden.backend.services.AdresseService;
import com.examen.gerden.backend.services.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UtilisateurServiceImpl implements UtilisateurService, UserDetailsService {
    private final UtilisateurRepository utilisateurRepository;
    private final RoleRepository roleRepository;
    private final AdresseService adresseService;
    private final AdresseRepository adresseRepository;
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UtilisateurServiceImpl(UtilisateurRepository utilisateurRepository,
                                  RoleRepository roleRepository,
                                  AdresseRepository adresseRepository,
                                  AdresseService adresseService,
                                  BCryptPasswordEncoder passwordEncoder){
        this.utilisateurRepository = utilisateurRepository;
        this.adresseRepository=adresseRepository;
        this.roleRepository=roleRepository;
        this.adresseService=adresseService;
        this.passwordEncoder=passwordEncoder;
    }

    public boolean validationEmail(Map<String,String> email){
        return utilisateurRepository.findByEmail(email.get("email"))
                .map(Utilisateur::getIsActif)
                .orElse(false);
    }

    public void enregistrerUtilisateur(Utilisateur utilisateur) {
        Optional<Utilisateur> utilisateurBDD = utilisateurRepository.findByEmail(utilisateur.getEmail());
        if (utilisateurBDD.isPresent()) {
            throw new ResourceAlreadyExistException("Vous avez déjà un compte associé à l'adresse email: " + utilisateur.getEmail());
        } else {
            // Création d'un nouvel utilisateur
            Utilisateur utilisateurPersiste = new Utilisateur();

            // Copie des informations de base
            utilisateurPersiste.setNom(utilisateur.getNom());
            utilisateurPersiste.setPrenom(utilisateur.getPrenom());
            utilisateurPersiste.setEmail(utilisateur.getEmail());
            utilisateurPersiste.setTelephone(utilisateur.getTelephone());
            utilisateurPersiste.setFonction(utilisateur.getFonction());

            // Vérification et assignation du rôle
            Role role = roleRepository.findByLibelle(utilisateur.getRole().getLibelle());
            utilisateurPersiste.setRole(Objects.requireNonNullElseGet(role, () -> roleRepository.save(utilisateur.getRole())));

            // Vérification et assignation de l'adresse
            Optional<Adresse> adresseOptional = adresseRepository.findByAdresseComplete(
                    utilisateur.getAdresse().getAdresseComplete()
            ).stream().findFirst();

            Adresse adresse = adresseOptional.orElseGet(() -> adresseRepository.save(utilisateur.getAdresse()));
            utilisateurPersiste.setAdresse(adresse);

            // Encrypte le mot de passe
            String motdepasseCrypte = passwordEncoder.encode(utilisateur.getMotDePasse());
            utilisateurPersiste.setMotDePasse(motdepasseCrypte);

            // Sauvegarde de l'utilisateur
            utilisateurRepository.save(utilisateurPersiste);
        }
    }

    @Override
    public Utilisateur modifierUtilisateur(Utilisateur utilisateur) {
        //TODO

        return utilisateur;
    }

    public List<Utilisateur> recupererTousLesUtilisateurs() {
        return utilisateurRepository.findAll();
    }

    public Optional<Utilisateur> recupererUtilisateur(Long id) {
        return utilisateurRepository.findById(id);
    }

    public List<Utilisateur> recupererUtilisateurParRole(Role role) {
        return utilisateurRepository.findByRole(role);
    }

    public void supprimerUtilisateur(Long id) {
        utilisateurRepository.deleteById(id);
    }

    @Override
    public Utilisateur loadUserByUsername(String username) throws UsernameNotFoundException {
        return utilisateurRepository.findByEmail(username).orElseThrow(
                ()-> new UsernameNotFoundException("Aucun utilisateur ne correspond à cette identifiant"));

    }

    public void modifierMotDePasse(Map<String, String> parametres) {
        Utilisateur utilisateur = this.loadUserByUsername(parametres.get("email"));
        String motdepasseCrypte = passwordEncoder.encode(parametres.get("password"));
        utilisateur.setMotDePasse(motdepasseCrypte);
        utilisateurRepository.save(utilisateur);
    }

    public void nouveauMotDePasse(Map<String, String> parametres) {
        Utilisateur utilisateur = this.loadUserByUsername(parametres.get("email"));
        String motdepasseCrypte = passwordEncoder.encode(parametres.get("password"));
        utilisateur.setMotDePasse(motdepasseCrypte);
        utilisateurRepository.save(utilisateur);
    }

}
