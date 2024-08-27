package com.examen.gerden.backend.services.impl;

import com.examen.gerden.backend.models.Jwt;
import com.examen.gerden.backend.models.RefreshToken;
import com.examen.gerden.backend.models.Utilisateur;
import com.examen.gerden.backend.repositories.JwtRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
@Slf4j
@Transactional
public class JwtServiceImpl {
    public static final String REFRESH = "refresh";
    public static final String TOKEN_INVALIDE = "Token invalide";
    private final String ENCRIPTION_KEY = "608f36e92dc66d97d5933f0e6371493cb4fc05b1aa8f8de64014732472303a7c";
    private UtilisateurServiceImpl utilisateurService;
    private JwtRepository jwtRepository;

    Jwt tokenByValue(String value){
        return jwtRepository.findByValeur(value).orElseThrow(() -> new RuntimeException("Token inconnu"));
    }

    public Map<String, String> generate(String username) {
        Utilisateur utilisateur = this.utilisateurService.loadUserByUsername(username);
        this.disableTokens(utilisateur);
        final Map<String, String> jwtMap = this.generateJwt(utilisateur);

        RefreshToken refreshToken=RefreshToken.builder()
                .valeur(UUID.randomUUID().toString())
                .estExpire(false)
                .creation(Instant.now())
                .expiration(Instant.now().plusMillis(30*60*1000))
                .build();

        final Jwt accessToken=Jwt
                .builder()
                .estDesactive(false)
                .estExpire(false)
                .valeur(jwtMap.get("accessToken"))
                .refreshToken(refreshToken)
                .utilisateur(utilisateur)
                .build();
        this.jwtRepository.save(accessToken);
        jwtMap.put(REFRESH, refreshToken.getValeur());

        return jwtMap;
    }
    private void disableTokens(Utilisateur utilsateur){
        final List<Jwt> jwtList= this.jwtRepository.findUtilisateur(utilsateur.getEmail()).peek(
                jwt -> {
                    jwt.setEstExpire(true);
                    jwt.setEstDesactive(true);
                }
        ).collect(Collectors.toList());
        this.jwtRepository.saveAll(jwtList);
    }

    public String extractUsername(String token) {
        return this.getClaim(token, Claims::getSubject);
    }

    public boolean isTokenExpired(String token) {
        Date expirationDate = getExpirationDateFromToken(token);
        return expirationDate.before(new Date());
    }

    private Date getExpirationDateFromToken(String token) {
        return this.getClaim(token, Claims::getExpiration);
    }

    private <T> T getClaim(String token, Function<Claims, T> function) {
        Claims claims = getAllClaims(token);
        return function.apply(claims);
    }

    private Claims getAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(this.getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Map<String, String> generateJwt(Utilisateur utilisateur) {
        final long currentTime = System.currentTimeMillis();
        final long expirationTimeAccessToken = currentTime + 60 * 1000;


        final Map<String, Object> claimsAccess = Map.of(
                "idUtilisateur", utilisateur.getId(),
                "nom", utilisateur.getNom()+ " "+utilisateur.getPrenom(),
                Claims.EXPIRATION, new Date(expirationTimeAccessToken),
                Claims.SUBJECT, utilisateur.getEmail()
        );


        final String accessToken = Jwts.builder()
                .setIssuer("https://www.asso-lea.org/")
                .setIssuedAt(new Date(currentTime))
                .setExpiration(new Date(expirationTimeAccessToken))
                .setSubject(utilisateur.getEmail())
                .setClaims(claimsAccess)
                .signWith(SignatureAlgorithm.HS256,getKey())
                .compact();

        Map<String, String> jwt = new HashMap<>();
        jwt.put("accessToken", accessToken);

        return jwt;
    }

    private Key getKey() {
        final byte[] decoder = Decoders.BASE64.decode(ENCRIPTION_KEY);
        return Keys.hmacShaKeyFor(decoder);
    }

    public void deconnexion(){
        Utilisateur utilisateurConnecte = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Jwt jwt = this.jwtRepository.findUtilisateurValidToken(utilisateurConnecte.getEmail(), false,false).orElseThrow(() -> new RuntimeException(TOKEN_INVALIDE));
        jwt.setEstDesactive(true);
        jwt.setEstExpire(true);
        this.jwtRepository.save(jwt);
    }
    @Scheduled(cron = "@daily")
    public void removeExpireAndDesactiveJwt(){
        log.info("Supression des token unitile à {}", Instant.now());
        this.jwtRepository.deleteAllByEstExpireAndEstDesactive(true,true);
    }

    public Map<String,String> refreshToken(Map<String,String> refreshTokenRequest) {
        final Jwt jwt = this.jwtRepository.findByRefreshTokenValeur(refreshTokenRequest.get(REFRESH)).orElseThrow(() -> new RuntimeException(TOKEN_INVALIDE));

        if(jwt.getRefreshToken().isEstExpire() || jwt.getRefreshToken().getExpiration().isBefore(Instant.now())){
            throw new RuntimeException(TOKEN_INVALIDE);
        }
        this.disableTokens(jwt.getUtilisateur());
        this.jwtRepository.deleteByRefreshTokenValeur(refreshTokenRequest.get(REFRESH));
        return this.generate(jwt.getUtilisateur().getEmail());
    }
}
