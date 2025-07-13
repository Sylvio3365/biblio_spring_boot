package com.biblio.repository;

import com.biblio.model.Adherent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AdherentRepository extends JpaRepository<Adherent, Long> {

    @Query("""
                SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END
                FROM Sanction s
                WHERE s.adherent.id = :idAdherent
                AND CURRENT_TIMESTAMP BETWEEN s.debut AND s.fin
            """)
    boolean isSanctioned(@Param("idAdherent") Long idAdherent);

    @Query("""
                SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END
                FROM Actif a
                WHERE a.adherent.id = :idAdherent
                AND CURRENT_TIMESTAMP BETWEEN a.debut AND a.fin
            """)
    boolean isActif(@Param("idAdherent") Long idAdherent);
}
