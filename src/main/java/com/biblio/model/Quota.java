package com.biblio.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "quota")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Quota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idquotat;

    private Integer pret;

    private Integer reservation;

    private Integer prolongement;
}
