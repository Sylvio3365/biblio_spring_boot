package com.biblio.repository;

import com.biblio.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {

    Optional<Utilisateur> findByNom(String nom);

    Optional<Utilisateur> findByNomAndMdp(String nom, String mdp);
}
