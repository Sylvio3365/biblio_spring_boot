package com.biblio.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.biblio.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    boolean existsByNom(String nom);
}
