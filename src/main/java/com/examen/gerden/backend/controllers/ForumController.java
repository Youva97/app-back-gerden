package com.examen.gerden.backend.controllers;

import com.examen.gerden.backend.models.Commentaire;
import com.examen.gerden.backend.models.Forum;
import com.examen.gerden.backend.services.ForumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/forums")
public class ForumController {
    private final ForumService forumService;

    @Autowired
    public ForumController(ForumService forumService) {
        this.forumService = forumService;
    }

    @PostMapping("create")
    public ResponseEntity<Forum> creerForum(@RequestBody Forum forum) {
        Forum newForum = forumService.creerForum(forum);
        return new ResponseEntity<>(newForum, HttpStatus.CREATED);
    }

    @PostMapping("/{forumId}/utilisateurs/{utilisateurId}")
    public ResponseEntity<Forum> ajouterUtilisateurAuForum(@PathVariable Long forumId, @PathVariable Long utilisateurId) {
        Forum updatedForum = forumService.ajouterUtilisateurAuForum(forumId, utilisateurId);
        return new ResponseEntity<>(updatedForum, HttpStatus.OK);
    }

    @PostMapping("/{forumId}/commentaires/{utilisateurId}")
    public ResponseEntity<Commentaire> ajouterCommentaire(@PathVariable Long forumId,
                                                          @PathVariable Long utilisateurId,
                                                          @RequestBody Commentaire commentaire) {
        Commentaire newCommentaire = forumService.ajouterCommentaire(forumId, utilisateurId, commentaire);
        return new ResponseEntity<>(newCommentaire, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Set<Forum>> listerTousLesForums() {
        Set<Forum> forums = forumService.listerTousLesForums();
        return new ResponseEntity<>(forums, HttpStatus.OK);
    }

    @GetMapping("/{forumId}")
    public ResponseEntity<Forum> recupererForumParId(@PathVariable Long forumId) {
        Forum forum = forumService.recupererForumParId(forumId);
        return new ResponseEntity<>(forum, HttpStatus.OK);
    }

    @DeleteMapping("/{forumId}")
    public ResponseEntity<Void> supprimerForumParId(@PathVariable Long forumId) {
        forumService.supprimerForumParId(forumId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{forumId}/commentaires")
    public ResponseEntity<Set<Commentaire>> listerCommentairesDuForum(@PathVariable Long forumId) {
        Set<Commentaire> commentaires = forumService.listerCommentairesDuForum(forumId);
        return new ResponseEntity<>(commentaires, HttpStatus.OK);
    }

    @PutMapping("/commentaires/{commentaireId}")
    public ResponseEntity<Commentaire> modifierCommentaire(@PathVariable Long commentaireId, @RequestBody String contenu) {
        Commentaire updatedCommentaire = forumService.modifierCommentaire(commentaireId, contenu);
        return new ResponseEntity<>(updatedCommentaire, HttpStatus.OK);
    }

    @DeleteMapping("/commentaires/{commentaireId}")
    public ResponseEntity<Void> supprimerCommentaire(@PathVariable Long commentaireId) {
        forumService.supprimerCommentaire(commentaireId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/commentaires/{commentaireId}/signaler")
    public ResponseEntity<Commentaire> signalerCommentaire(@PathVariable Long commentaireId) {
        Commentaire signaledCommentaire = forumService.signalerCommentaire(commentaireId);
        return new ResponseEntity<>(signaledCommentaire, HttpStatus.OK);
    }
}
