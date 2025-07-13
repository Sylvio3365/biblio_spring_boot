package com.biblio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.biblio.model.Pret;

public interface PretRepository extends JpaRepository<Pret, Long> {

}