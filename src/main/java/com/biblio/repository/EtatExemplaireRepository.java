package com.biblio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.biblio.model.EtatExemplaire;

public interface EtatExemplaireRepository extends JpaRepository<EtatExemplaire, Long> {

    // EtatExemplaire
    // findTopByExemplaire_IdexemplaireOrderByIdetatExemplaireDesc(Long
    // idExemplaire);

    @Query(value = """
                SELECT *
                FROM etatexemplaire
                WHERE idexemplaire = :idExemplaire
                ORDER BY idetatexemplaire DESC
                LIMIT 1
            """, nativeQuery = true)
    EtatExemplaire findDernierEtatByExemplaireId(@Param("idExemplaire") Long idExemplaire);

}
