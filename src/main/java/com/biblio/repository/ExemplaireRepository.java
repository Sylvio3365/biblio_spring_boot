package com.biblio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.biblio.model.Exemplaire;

public interface ExemplaireRepository extends JpaRepository<Exemplaire, Long> {
    @Query(value = """
                SELECT CASE
                         WHEN ee.idetat = 1 THEN TRUE
                         ELSE FALSE
                       END
                FROM etatexemplaire ee
                WHERE ee.idexemplaire = :idExemplaire
                ORDER BY ee.dateheure DESC
                LIMIT 1
            """, nativeQuery = true)
    boolean estDisponible(@Param("idExemplaire") Long idExemplaire);

    @Query("""
                SELECT COALESCE((
                    SELECT CASE
                        WHEN ee.etat.idetat = 1 THEN 1 ELSE 0
                    END
                    FROM EtatExemplaire ee
                    WHERE ee.exemplaire.idexemplaire = :idexemplaire
                    ORDER BY ee.dateheure DESC
                    LIMIT 1
                ), 1)
            """)
    Integer getDisponibilite(@Param("idexemplaire") Long idexemplaire);

}