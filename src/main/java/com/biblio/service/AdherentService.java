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

    public String checkAdherent(Long idadherent) {
        boolean estSanctionne = adherentRepository.isSanctioned(idadherent);
        // boolean estActif = adherentRepository.isActif(idadherent);
        boolean estAbonne = adherentRepository.isAbonne(idadherent);
        Adherent a = adherentRepository.findById(idadherent).orElse(null);
        if (a == null) {
            return "❌ Adhérent non trouvé";
        } if (estSanctionne) {
            return "⛔ Adhérent sanctionné – Prêt refusé";
        }
        // if (!estActif) {
        //     return "⛔ Adhérent inactif – Prêt refusé";
        // }
        if (!estAbonne) {
            return "⛔ Adhérent non abonné – Prêt refusé";
        }
        return "✅ Adhérent valide – Prêt autorisé";
    }

    public boolean isPretAutorise(Long idadherent) {
        String message = checkAdherent(idadherent);
        return message.contains("✅");
    }

    public int getNbPretNonRendu(Long idadherent) {
        return adherentRepository.getNbPretNonRendu(idadherent);
    }

}
