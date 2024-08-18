package com.examen.gerden.backend.repositories;

import com.examen.gerden.backend.models.Adresse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdresseRepository extends JpaRepository<Adresse, Long> {
    Adresse findByAdresseComplete(String numeroRue, String nomRue, String complementAdresse, String codePostal, String ville, String pays);
}
