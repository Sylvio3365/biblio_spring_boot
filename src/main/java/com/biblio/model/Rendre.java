package com.biblio.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Rendre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idrendre;

    private LocalDateTime daterendu;

    @OneToOne
    @JoinColumn(name = "idpret", nullable = false, unique = true)
    private Pret pret;
}
