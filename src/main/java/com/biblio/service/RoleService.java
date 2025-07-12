package com.biblio.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biblio.model.Role;
import com.biblio.repository.RoleRepository;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    public Role findById(Long id) {
        return roleRepository.findById(id).orElse(null);
    }

    public Role create(Role role) {
        if (roleRepository.existsByNom(role.getNom())) {
            throw new IllegalArgumentException("Le rôle existe déjà !");
        }
        return roleRepository.save(role);
    }

    public void delete(Long id) {
        roleRepository.deleteById(id);
    }

    public Role update(Long id, Role newRole) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Rôle introuvable"));
        role.setNom(newRole.getNom());
        return roleRepository.save(role);
    }
}
