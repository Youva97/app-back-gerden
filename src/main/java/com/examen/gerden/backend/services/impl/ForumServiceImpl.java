package com.examen.gerden.backend.services.impl;

import com.examen.gerden.backend.models.Commentaire;
import com.examen.gerden.backend.models.Forum;
import com.examen.gerden.backend.models.Utilisateur;
import com.examen.gerden.backend.repositories.CommentaireRepository;
import com.examen.gerden.backend.repositories.ForumRepository;
import com.examen.gerden.backend.repositories.UtilisateurRepository;
import com.examen.gerden.backend.services.ForumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class ForumServiceImpl implements ForumService {
    private final ForumRepository forumRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final CommentaireRepository commentaireRepository;

    @Autowired
    public ForumServiceImpl(ForumRepository forumRepository, UtilisateurRepository utilisateurRepository,
                            CommentaireRepository commentaireRepository) {
        this.forumRepository = forumRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.commentaireRepository = commentaireRepository;
    }

    /**
     * @param forum
     * @return
     */
    public Forum creerForum(Forum forum) {
        return forumRepository.save(forum);
    }

    /**
     * @param forumId
     * @param utilisateurId
     * @return
     */
    public Forum ajouterUtilisateurAuForum(Long forumId, Long utilisateurId) {
        Forum forum = forumRepository.findById(forumId).orElseThrow(
                () -> new IllegalArgumentException("Forum non trouvé"));
        Utilisateur utilisateur = utilisateurRepository.findById(utilisateurId).orElseThrow(
                () -> new IllegalArgumentException("Utilisateur non trouvé"));
        forum.getUtilisateurs().add(utilisateur);
        return forumRepository.save(forum);
    }

    /**
     * @param forumId
     * @param utilisateurId
     * @param commentaire
     * @return
     */
    public Commentaire ajouterCommentaire(Long forumId, Long utilisateurId, Commentaire commentaire) {
        Forum forum = forumRepository.findById(forumId).orElseThrow(
                () -> new IllegalArgumentException("Forum non trouvé"));
        Utilisateur utilisateur = utilisateurRepository.findById(utilisateurId).orElseThrow(
                () -> new IllegalArgumentException("Utilisateur non trouvé"));
        commentaire.setForum(forum);
        commentaire.setUtilisateur(utilisateur);
        return commentaireRepository.save(commentaire);
    }

    /**
     * @return
     */
    public Set<Forum> listerTousLesForums() {
        return new HashSet<>(forumRepository.findAll());
    }

    /**
     * @param forumId
     * @return
     */
    public Forum recupererForumParId(Long forumId) {
        return forumRepository.findById(forumId).orElseThrow(() -> new IllegalArgumentException("Forum non trouvé"));
    }

    /**
     * @param forumId
     */
    public void supprimerForumParId(Long forumId) {
        forumRepository.deleteById(forumId);
    }

    /**
     * @param forumId
     * @return
     */
    public Set<Commentaire> listerCommentairesDuForum(Long forumId) {
        Forum forum = forumRepository.findById(forumId).orElseThrow(
                () -> new IllegalArgumentException("Forum non trouvé"));
        return forum.getListeDeCommentaires();
    }

    /**
     * @param commentaireId
     * @param contenu
     * @return
     */
    public Commentaire modifierCommentaire(Long commentaireId, String contenu) {
        Commentaire commentaire = commentaireRepository.findById(commentaireId).orElseThrow(
                () -> new IllegalArgumentException("Commentaire non trouvé"));
        commentaire.setContenu(contenu);
        return commentaireRepository.save(commentaire);
    }

    /**
     * @param commentaireId
     */
    public void supprimerCommentaire(Long commentaireId) {
        commentaireRepository.deleteById(commentaireId);
    }

    /**
     * @param commentaireId
     * @return
     */
    public Commentaire signalerCommentaire(Long commentaireId) {
        Commentaire commentaire = commentaireRepository.findById(commentaireId).orElseThrow(
                () -> new IllegalArgumentException("Commentaire non trouvé"));
        commentaire.setSignale(true);
        return commentaireRepository.save(commentaire);
    }
}
