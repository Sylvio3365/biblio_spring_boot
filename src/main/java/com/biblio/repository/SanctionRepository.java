package com.biblio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.biblio.model.Sanction;

public interface SanctionRepository extends JpaRepository<Sanction, Long>   {
    
}
