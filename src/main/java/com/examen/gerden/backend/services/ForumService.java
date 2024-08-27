package com.examen.gerden.backend.services;

import com.examen.gerden.backend.models.Commentaire;
import com.examen.gerden.backend.models.Forum;

import java.util.Set;

public interface ForumService {
    /**
     * Créer un nouveau forum.
     *
     * @param forum le forum à créer
     * @return le forum crée
     */
    Forum creerForum(Forum forum);

    /**
     * Ajouter un utilisateur à un forum.
     *
     * @param forumId       l'Id du forum
     * @param utilisateurId l'Id de l'utilisateur à rajouter
     * @return le forum trouvé
     */
    Forum ajouterUtilisateurAuForum(Long forumId, Long utilisateurId);

    /**
     * Ajouter un commentaire à un forum.
     *
     * @param forumId       l'Id du forum
     * @param utilisateurId l'Id de l'utilisateur
     * @param commentaire   le commentaire à rajouter
     * @return le forum trouvé
     */
    Commentaire ajouterCommentaire(Long forumId, Long utilisateurId, Commentaire commentaire);

    /**
     * Lister tous les forums.
     *
     * @return la liste des formus
     */
    Set<Forum> listerTousLesForums();

    /**
     * Récupérer un forum par ID.
     *
     * @param forumId l'Id du forum
     * @return le formum trouvé
     */
    Forum recupererForumParId(Long forumId);

    /**
     * Supprimer un forum par ID.
     *
     * @param forumId l'Id du forum
     */
    void supprimerForumParId(Long forumId);

    /**
     * Lister les commentaires d'un forum.
     *
     * @param forumId l'Id du forum
     * @return la liste des commentaires
     */
    Set<Commentaire> listerCommentairesDuForum(Long forumId);

    /**
     * Modifier un commentaire.
     *
     * @param commentaireId l'Id de commentaire
     * @param contenu       le contenu à ajouter
     * @return le commentaire modifié
     */
    Commentaire modifierCommentaire(Long commentaireId, String contenu);

    /**
     * Supprimer un commentaire.
     *
     * @param commentaireId l'Id de commentaire
     */
    void supprimerCommentaire(Long commentaireId);

    /**
     * Signaler un commentaire.
     *
     * @param commentaireId l'Id de commentaire
     * @return le commentaire signalé
     */
    Commentaire signalerCommentaire(Long commentaireId);
}
