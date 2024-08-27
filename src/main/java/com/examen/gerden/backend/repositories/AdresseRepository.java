package com.examen.gerden.backend.repositories;

import com.examen.gerden.backend.models.Adresse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdresseRepository extends JpaRepository<Adresse, Long> {
    @Query("SELECT a FROM Adresse a WHERE CONCAT(a.numeroRue, ' ', a.nomRue, ' ', a.complementAdresse, ', ', a.ville, ' ', a.codePostal, ', ', a.pays) = :adresseComplete")
    List<Adresse> findByAdresseComplete(@Param("adresseComplete") String adresseComplete);

}
