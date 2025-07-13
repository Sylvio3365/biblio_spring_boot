package com.biblio.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biblio.model.Exemplaire;
import com.biblio.repository.ExemplaireRepository;

@Service
public class ExemplaireService {

    @Autowired
    public ExemplaireRepository exemplaireRepository;

    public List<Exemplaire> findAll() {
        return exemplaireRepository.findAll();
    }

    public boolean estDisponible(Long idexemplaire) {
        Integer dispo = exemplaireRepository.getDisponibilite(idexemplaire);
        return dispo != null && dispo == 1;
    }

    public Exemplaire findById(Long idexemplaire) {
        return exemplaireRepository.findById(idexemplaire).orElse(null);
    }
}
