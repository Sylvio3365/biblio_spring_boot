package com.biblio.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "reglejf")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegleJF {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idreglejf;

    private Integer comportement;
}
