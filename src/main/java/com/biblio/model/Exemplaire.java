package com.biblio.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "exemplaire")
public class Exemplaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idexemplaire;

    private String numero;

    @ManyToOne
    @JoinColumn(name = "idtypepret")
    private TypePret typePret;

    @ManyToOne
    @JoinColumn(name = "idlivre")
    private Livre livre;
}
