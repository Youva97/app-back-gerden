package com.examen.gerden.backend.dtos;

import com.examen.gerden.backend.models.Adresse;
import com.examen.gerden.backend.models.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UtilisateurDTO {
    private Long id;
    @NotBlank(message = "Le nom est obligatoire")
    private String nom;
    @NotBlank(message = "Le prenom est obligatoire")
    private String prenom;
    @NotBlank(message = "L'email est obligatoire")
    @Email
    private String email;
    @NotBlank(message = "Le mot de passe est obligatoire")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String motDePasse;
    @NotBlank(message = "La fonction est obligatoire")
    private String fonction;
    @NotBlank(message = "Le numero de telephone est obligatoire")
    private String telephone;
    @NotNull(message = "Le Role est obligatoire")
    private Role role;
    @NotNull(message = "L'adresse est obligatoire")
    private Adresse adresse;
}
