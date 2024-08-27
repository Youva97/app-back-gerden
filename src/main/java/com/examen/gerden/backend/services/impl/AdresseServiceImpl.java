package com.examen.gerden.backend.services.impl;

import com.examen.gerden.backend.exceptions.ResourceNotFoundException;
import com.examen.gerden.backend.models.Adresse;
import com.examen.gerden.backend.repositories.AdresseRepository;
import com.examen.gerden.backend.services.AdresseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdresseServiceImpl implements AdresseService {
    private final AdresseRepository adresseRepository;

    @Override
    public void enregistrerAdresse(Adresse adresse) {
        adresseRepository.save(adresse);
    }

    @Override
    public Adresse adresseExiste(final String numeroRue,
                                 final String nomRue,
                                 final String complementAdresse,
                                 final String codePostal,
                                 final String ville,
                                 final String pays) {

        // Construire l'adresse complète en tenant compte du complément d'adresse
        String adresseComplete = numeroRue + " " + nomRue + " " +
                (complementAdresse != null ? complementAdresse + ", " : "") +
                ville + " " + codePostal + ", " + pays;

        // Rechercher l'adresse dans la base de données
        List<Adresse> adresses = adresseRepository.findByAdresseComplete(adresseComplete);

        // Retourner la première adresse trouvée ou lever une exception si aucune n'est trouvée
        if (adresses.isEmpty()) {
            throw new ResourceNotFoundException("Aucune adresse correspondante trouvée.");
        }
        return adresses.get(0);
    }


    @Override
    public Optional<Adresse> recupererAdresse(Long id) {
        return adresseRepository.findById(id);
    }
}
