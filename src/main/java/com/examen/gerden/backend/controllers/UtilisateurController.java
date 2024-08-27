package com.examen.gerden.backend.controllers;

import com.examen.gerden.backend.dtos.AuthentificationDTO;
import com.examen.gerden.backend.dtos.UtilisateurDTO;
import com.examen.gerden.backend.mapper.UtilisateurMapper;
import com.examen.gerden.backend.models.Role;
import com.examen.gerden.backend.models.Utilisateur;
import com.examen.gerden.backend.services.impl.AdresseServiceImpl;
import com.examen.gerden.backend.services.impl.JwtServiceImpl;
import com.examen.gerden.backend.services.impl.UtilisateurServiceImpl;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.examen.gerden.backend.dtos.MyHttpResponse.response;

@RestController
@Slf4j
@RequestMapping("/utilisateurs")
@CrossOrigin
public class UtilisateurController {
    private final UtilisateurServiceImpl utilisateurService;
    private final AdresseServiceImpl adresseService;
    private AuthenticationManager authenticationManager;

    final JwtServiceImpl jwtServiceImpl;
    @Autowired
    UtilisateurMapper utilisateurMapper;

    @Autowired
    public UtilisateurController(UtilisateurServiceImpl utilisateurService,
                                 AdresseServiceImpl adresseService,
                                 AuthenticationManager authenticationManager, JwtServiceImpl jwtServiceImpl)
    {
        this.utilisateurService = utilisateurService;
        this.adresseService = adresseService;
        this.authenticationManager=authenticationManager;
        this.jwtServiceImpl = jwtServiceImpl;
    }

    //*******************GESTION DE L'UTILISATEUR******************************
    @GetMapping("/validation-email")
    public ResponseEntity<Object> validationEmail(@RequestParam String email) {
        final Map<String, String> emailMap = Map.of("email", email);
        if(utilisateurService.validationEmail(emailMap)){
            return response(HttpStatus.CONFLICT, "Vous avez déjà un compte associé cette adresse mail",null);
        }else {
            return response(HttpStatus.ACCEPTED, "Votre email est valide",null);
        }
    }
    @PostMapping("/inscription")
    public ResponseEntity<Object> ajouterUtilisateur(@RequestBody @Valid UtilisateurDTO utilisateurDTO) {
        log.info("Inscription");
        Utilisateur nouveauUtilisateur=utilisateurMapper.toEntity(utilisateurDTO);
        utilisateurService.enregistrerUtilisateur(nouveauUtilisateur);
        return response(HttpStatus.CREATED, "Votre compte a été créer, veuillez consulter votre e-mail pour l'activé",null);
    }

    @PostMapping("/connexion")
    public Map<String,String> connexion(@RequestBody AuthentificationDTO authentificationDTO) {
        final Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authentificationDTO.username(), authentificationDTO.password())
        );
        if(authenticate.isAuthenticated()){
            return  jwtServiceImpl.generate(authentificationDTO.username());
        }
        return null;
    }
    @PostMapping("/refresh-token")
    public @ResponseBody Map<String,String> refreshToken (@RequestBody Map<String,String> refreshTokenRequest) {
        return jwtServiceImpl.refreshToken(refreshTokenRequest);
    }

    @PostMapping("/deconnexion")
    public void deconnexion() {
        jwtServiceImpl.deconnexion();
    }

    @PostMapping("/modifier-mot-de-passe")
    public void modifierMotDePasse(@RequestBody Map<String, String> parametres) {
        utilisateurService.modifierMotDePasse(parametres);
    }
    @PostMapping("/nouveau-mot-de-passe")
    public void nouveauMotDePasse(@RequestBody Map<String, String> parametres) {
        utilisateurService.nouveauMotDePasse(parametres);
    }


    @PutMapping("/modifier")
    public ResponseEntity<Utilisateur> modifierUtilisateur(@RequestBody Utilisateur utilisateur) {
        Utilisateur utilisateurModifie = utilisateurService.modifierUtilisateur(utilisateur);
        return new ResponseEntity<>(utilisateurModifie, HttpStatus.OK);
    }

    @GetMapping("/tous")
    public ResponseEntity<List<Utilisateur>> recupererTousLesUtilisateurs() {
        List<Utilisateur> utilisateurs = utilisateurService.recupererTousLesUtilisateurs();
        return new ResponseEntity<>(utilisateurs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Utilisateur> recupererUtilisateur(@PathVariable Long id) {
        Optional<Utilisateur> utilisateur = utilisateurService.recupererUtilisateur(id);
        return utilisateur.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerUtilisateur(@PathVariable Long id) {
        Optional<Utilisateur> utilisateur = utilisateurService.recupererUtilisateur(id);
        if (utilisateur.isPresent()) {
            utilisateurService.supprimerUtilisateur(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/role")
    public ResponseEntity<List<Utilisateur>> recupererUtilisateurParRole(Role role) {
        List<Utilisateur> professionnels = utilisateurService.recupererUtilisateurParRole(role);
        return new ResponseEntity<>(professionnels, HttpStatus.OK);
    }

}
