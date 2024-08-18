package com.examen.gerden.backend.repositories;

import com.examen.gerden.backend.enums.TypeDeRole;
import com.examen.gerden.backend.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository  extends JpaRepository<Role, Long> {
    Role findByLibelle(TypeDeRole libelle);
}
