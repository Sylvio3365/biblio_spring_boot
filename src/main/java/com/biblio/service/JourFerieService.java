package com.biblio.service;

import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.biblio.repository.JourFerieRepository;

@Service
public class JourFerieService {

    @Autowired
    private JourFerieRepository jourFerieRepository;

    public boolean isJourFerie(LocalDate date) {
        return jourFerieRepository.existsByDatejf(date);
    }
}
