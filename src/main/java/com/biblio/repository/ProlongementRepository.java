package com.biblio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.biblio.model.Prolongement;

public interface ProlongementRepository extends JpaRepository<Prolongement, Long> {

    @Query("""
                SELECT p FROM Prolongement p
                WHERE p.pret.idpret = :idPret
            """)
    Prolongement findByIdPret(@Param("idPret") Long idPret);

    @Query("""
                SELECT p FROM Prolongement p
                WHERE (
                    SELECT sp.statut.idstatut
                    FROM StatutProlongement sp
                    WHERE sp.prolongement.idprolongement = p.idprolongement
                    ORDER BY sp.datemodif DESC
                    LIMIT 1
                ) = 1
            """)
    List<Prolongement> findProlongementEnAttente();

}
