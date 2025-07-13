package com.biblio.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "abonnement")
public class Abonnement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idabonnement;

    private LocalDateTime debut;

    private LocalDateTime fin;

    @ManyToOne
    @JoinColumn(name = "idadherent", nullable = false)
    private Adherent adherent;
}
