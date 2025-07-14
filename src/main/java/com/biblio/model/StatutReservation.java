package com.biblio.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Table(name = "statutreservation")
@Data
@NoArgsConstructor
@AllArgsConstructor
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
