package com.biblio.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idreservation;

    private LocalDateTime datereservation;

    private LocalDate datepret;

    @ManyToOne
    @JoinColumn(name = "idexemplaire", nullable = false)
    private Exemplaire exemplaire;

    @ManyToOne
    @JoinColumn(name = "idadherent", nullable = false)
    private Adherent adherent;
}
