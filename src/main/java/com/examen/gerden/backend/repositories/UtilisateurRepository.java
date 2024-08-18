package com.examen.gerden.backend.repositories;

import com.examen.gerden.backend.models.Role;
import com.examen.gerden.backend.models.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    List<Utilisateur> findByRole(Role role);
    boolean existsByEmail(String email);
    Optional<Utilisateur> findByEmail(String email);
}
