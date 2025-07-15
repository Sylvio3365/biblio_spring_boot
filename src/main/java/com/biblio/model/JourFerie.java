package com.biblio.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "jourferie")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JourFerie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idjourferie;

    private LocalDate datejf;
}
