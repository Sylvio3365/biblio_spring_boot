package com.biblio.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biblio.model.Adherent;
import com.biblio.repository.AdherentRepository;

@Service
public class AdherentService {

    @Autowired
    private AdherentRepository adherentRepository;

    public boolean isSanctioned(Long idadherent) {
        return adherentRepository.isSanctioned(idadherent);
    }

    public boolean isActif(Long idadherent) {
        return adherentRepository.isActif(idadherent);
    }

    public List<Adherent> findAll() {
        return adherentRepository.findAll();
    }

    public Adherent findById(Long idadherent) {
        return adherentRepository.findById(idadherent).orElse(null);
    }
}
