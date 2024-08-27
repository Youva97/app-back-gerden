package com.examen.gerden.backend.repositories;

import com.examen.gerden.backend.models.Commentaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentaireRepository extends JpaRepository<Commentaire, Long>{
}
