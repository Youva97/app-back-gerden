package com.examen.gerden.backend.repositories;

import com.examen.gerden.backend.models.Jwt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface JwtRepository extends JpaRepository<Jwt, Long> {
    Optional<Jwt> findByValeur(String valeur);
    @Query("FROM Jwt j WHERE j.estExpire= :estExpire AND j.estDesactive = :estDesactive AND j.utilisateur.email = :email")
    Optional<Jwt> findUtilisateurValidToken(String email, boolean estDesactive, boolean estExpire );

    @Query("FROM Jwt j WHERE j.utilisateur.email = :email")
    Stream<Jwt> findUtilisateur(String email);

    @Query("FROM Jwt j WHERE j.refreshToken.valeur = :valeur")
    Optional<Jwt> findByRefreshTokenValeur(String valeur);

    void deleteAllByEstExpireAndEstDesactive(boolean estExpire, boolean estDesactive);


    void deleteByRefreshTokenValeur(String s);
}
