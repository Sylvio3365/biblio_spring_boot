package com.biblio.repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.biblio.model.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("""
                SELECT r
                FROM Reservation r
                WHERE (
                    SELECT sr.statut.idstatut
                    FROM StatutReservation sr
                    WHERE sr.reservation.idreservation = r.idreservation
                    ORDER BY sr.datemodif DESC
                    LIMIT 1
                ) = 1
            """)
    List<Reservation> findAllReservationsEnAttente();

    @Query("""
                SELECT r
                FROM Reservation r
                WHERE r.datepret = :date
                AND (
                    SELECT sr.statut.idstatut
                    FROM StatutReservation sr
                    WHERE sr.reservation.idreservation = r.idreservation
                    ORDER BY sr.datemodif DESC
                    LIMIT 1
                ) = 1
            """)
    List<Reservation> findAllReservationsEnAttenteAvalider(LocalDate date);

}
