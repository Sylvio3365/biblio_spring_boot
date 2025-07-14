package com.biblio.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biblio.model.Adherent;
import com.biblio.model.Pret;
import com.biblio.model.Prolongement;
import com.biblio.model.Quota;
import com.biblio.model.Regle;
import com.biblio.model.StatutProlongement;
import com.biblio.repository.ProlongementRepository;

@Service
public class ProlongementService {

    @Autowired
    private ProlongementRepository prolongementRepository;
    @Autowired
    private AdherentService adherentService;
    @Autowired
    private StatutProlongementService statutProlongementService;

    public Prolongement findById(Long id) {
        return prolongementRepository.findById(id).orElse(null);
    }

    public List<Prolongement> findProlongementEnAttente() {
        return prolongementRepository.findProlongementEnAttente();
    }

    public Prolongement findByIdPret(Long idpret) {
        return prolongementRepository.findByIdPret(idpret);
    }

    public Prolongement save(Prolongement p) {
        return prolongementRepository.save(p);
    }

    public String demandeProlongationPret(Pret pret, LocalDateTime nouveaufin) throws Exception {

        Adherent adherent = pret.getAdherent();

        adherentService.checkAdherent(adherent.getIdadherent());

        if (nouveaufin.isBefore(pret.getFin())) {
            throw new Exception("⛔ La nouvelle date prévue est antérieure à la date de fin actuelle du prêt.");
        }

        Regle regle = adherent.getProfil().getRegle();
        Quota quota = adherent.getProfil().getQuota();

        LocalDateTime vraifin = pret.getFin().plusDays(regle.getNbjourprolongement());

        if (nouveaufin.isAfter(vraifin)) {
            throw new Exception("⛔ La nouvelle date dépasse la durée maximale autorisée pour le prolongement.");
        }

        int nbProlongementsEnAttente = adherentService.getNbProlongementEnAttente(adherent.getIdadherent());

        if (nbProlongementsEnAttente >= quota.getProlongement()) {
            throw new Exception("⛔ Quota de prolongements en attente atteint (" + quota.getProlongement() + ").");
        }

        Prolongement prolongement = new Prolongement();
        prolongement.setPret(pret);
        prolongement.setNouveaufin(nouveaufin);
        this.save(prolongement);

        StatutProlongement sp = new StatutProlongement();
        sp.setProlongement(prolongement);
        sp.setDatemodif(LocalDateTime.now());

        statutProlongementService.saveAvecStatut(sp, 1);
        return "✅ Prêt prolongé avec succès.";
    }

    // public
}
