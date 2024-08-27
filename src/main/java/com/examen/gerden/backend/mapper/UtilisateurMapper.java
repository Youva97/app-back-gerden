package com.examen.gerden.backend.mapper;

import com.examen.gerden.backend.dtos.UtilisateurDTO;
import com.examen.gerden.backend.models.Utilisateur;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel="spring")
public interface UtilisateurMapper {
    Utilisateur toEntity(UtilisateurDTO utilisateurDTO);
    UtilisateurDTO toDto(Utilisateur utilisateur);
    void copy(UtilisateurDTO utilisateurDTO, @MappingTarget Utilisateur utilisateur);
}
