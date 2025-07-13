package com.biblio.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Prolongement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idprolongement;

    private LocalDateTime nouveaufin;

    @OneToOne
    @JoinColumn(name = "idpret", nullable = false, unique = true)
    private Pret pret;
}
