package com.examen.gerden.backend.services.impl;

import com.examen.gerden.backend.models.Role;
import com.examen.gerden.backend.repositories.RoleRepository;
import com.examen.gerden.backend.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;

public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository){
        this.roleRepository=roleRepository;

    }
    @Override
    public void enregistrerRole(Role role) {
        roleRepository.save(role);
    }
}
