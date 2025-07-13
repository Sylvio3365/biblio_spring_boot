package com.biblio.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class StatutReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idstatutreservation;

    private LocalDateTime datemodif;

    @ManyToOne
    @JoinColumn(name = "idstatut", nullable = false)
    private Statut statut;

    @ManyToOne
    @JoinColumn(name = "idreservation", nullable = false)
    private Reservation reservation;
}
