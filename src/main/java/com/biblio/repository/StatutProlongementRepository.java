package com.biblio.repository;

import com.biblio.model.StatutProlongement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StatutProlongementRepository extends JpaRepository<StatutProlongement, Long> {

    @Query("""
                SELECT sp FROM StatutProlongement sp
                WHERE sp.prolongement.idprolongement = :idProlongement
                ORDER BY sp.datemodif DESC
                LIMIT 1
            """)
    StatutProlongement findDernierStatutByIdProlongement(@Param("idProlongement") Long idProlongement);
}
