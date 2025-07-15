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

    @Query("""
                SELECT CASE WHEN COUNT(ab) > 0 THEN true ELSE false END
                FROM Abonnement ab
                WHERE ab.adherent.id = :idAdherent
                AND CURRENT_TIMESTAMP BETWEEN ab.debut AND ab.fin
            """)
    boolean isAbonne(@Param("idAdherent") Long idAdherent);

    @Query("""
                SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END
                FROM Sanction s
                WHERE s.adherent.id = :idAdherent
                AND :datetime BETWEEN s.debut AND s.fin
            """)
    boolean isSanctionedAtDateTime(@Param("idAdherent") Long idAdherent,
            @Param("datetime") java.time.LocalDateTime datetime);

    @Query("""
                SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END
                FROM Actif a
                WHERE a.adherent.id = :idAdherent
                AND :datetime BETWEEN a.debut AND a.fin
            """)
    boolean isActifAtDateTime(@Param("idAdherent") Long idAdherent,
            @Param("datetime") java.time.LocalDateTime datetime);

    @Query("""
                SELECT CASE WHEN COUNT(ab) > 0 THEN true ELSE false END
                FROM Abonnement ab
                WHERE ab.adherent.id = :idAdherent
                AND :datetime BETWEEN ab.debut AND ab.fin
            """)
    boolean isAbonneAtDateTime(@Param("idAdherent") Long idAdherent,
            @Param("datetime") java.time.LocalDateTime datetime);

    @Query("""
                SELECT COUNT(p)
                FROM Pret p
                WHERE p.adherent.idadherent = :idAdherent
                AND p.idpret NOT IN (SELECT r.pret.idpret FROM Rendre r)
            """)
    int getNbPretNonRendu(@Param("idAdherent") Long idAdherent);

    @Query("""
                SELECT a
                FROM Adherent a
                WHERE a.utilisateur.idutilisateur = :idUtilisateur
            """)
    Adherent findByIdutilisateur(@Param("idUtilisateur") Long idUtilisateur);

    @Query("""
                SELECT COUNT(r)
                FROM Reservation r
                WHERE r.adherent.idadherent = :idAdherent
                AND (
                    SELECT sr.statut.idstatut
                    FROM StatutReservation sr
                    WHERE sr.reservation.idreservation = r.idreservation
                    ORDER BY sr.datemodif DESC
                    LIMIT 1
                ) = 1
            """)
    int countReservationEnAttente(@Param("idAdherent") Long idAdherent);

    @Query(value = """
            SELECT COUNT(p.idprolongement)
            FROM prolongement p
            JOIN pret pr ON p.idpret = pr.idpret
            WHERE pr.idadherent = :idAdherent
            AND (
                SELECT ps.idstatut
                FROM prolongementstatut ps
                WHERE ps.idprolongement = p.idprolongement
                ORDER BY ps.datemodif DESC
                LIMIT 1
            ) = 1
            """, nativeQuery = true)
    int getNbProlongementEnAttente(@Param("idAdherent") Long idAdherent);

}
