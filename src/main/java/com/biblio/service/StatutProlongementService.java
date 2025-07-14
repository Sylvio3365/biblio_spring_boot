package com.biblio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import com.biblio.model.Statut;
import com.biblio.model.StatutProlongement;
import com.biblio.repository.StatutProlongementRepository;

@Service
public class StatutProlongementService {

    @Autowired
    public StatutProlongementRepository statutProlongementRepository;

    public StatutProlongement save(StatutProlongement st) {
        return statutProlongementRepository.save(st);
    }

    // +----------+---------------------+
    // | idstatut | nom
    // +----------+---------------------+
    // | 1 | En attente
    // | 2 | Valider
    // | 3 | Transformer en pret
    // | 4 | Annuler
    // +----------+---------------------+

    public StatutProlongement saveAvecStatut(StatutProlongement st, int statut) {
        if (statut == 1) {
            st.setStatut(new Statut(1L, "En attente"));
        }
        if (statut == 2) {
            st.setStatut(new Statut(2L, "Valider"));
        }
        if (statut == 4) {
            st.setStatut(new Statut(4L, "Annuler"));
        }
        return this.save(st);
    }

    public StatutProlongement findDernierStatutByIdProlongement(Long idProlongement) {
        return statutProlongementRepository.findDernierStatutByIdProlongement(idProlongement);
    }
}
