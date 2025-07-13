package com.biblio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biblio.model.StatutReservation;
import com.biblio.repository.StatutReservationRepository;

@Service
public class StatutReservationService {

    @Autowired
    private StatutReservationRepository statutReservationRepository;

    public StatutReservation save(StatutReservation sr) {
        return statutReservationRepository.save(sr);
    }
}
