package com.examen.gerden.backend.services;

import com.examen.gerden.backend.models.Adresse;

import java.util.Optional;

public interface AdresseService {
    void enregistrerAdresse(Adresse adresse);

    Adresse adresseExiste(String numeroRue, String nomRue, String complementAdresse, String codePostal, String ville, String pays);

    Optional<Adresse> recupererAdresse(Long id);
}
