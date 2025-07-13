package com.biblio.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biblio.model.Adherent;
import com.biblio.model.Pret;
import com.biblio.model.Rendre;
import com.biblio.model.Sanction;
import com.biblio.repository.RendreRepository;

@Service
public class RendreService {

    @Autowired
    private RendreRepository rendreRepository;
    @Autowired
    private AdherentService adherentService;
    @Autowired
    private SanctionService sanctionService;

    public Rendre save(Rendre r) {
        return rendreRepository.save(r);
    }

    public String rendrePret(Pret pret) {
        LocalDateTime today = LocalDateTime.now().plusHours(3);
        LocalDateTime dateFin = pret.getFin();

        // 🔁 Enregistrement du rendu
        Rendre rendu = new Rendre();
        rendu.setPret(pret);
        rendu.setDaterendu(today);
        rendreRepository.save(rendu); // Assure-toi que ce repository existe

        // ⏳ Vérification de retard
        if (today.isAfter(dateFin)) {
            Adherent adherent = adherentService.findById(pret.getAdherent().getIdadherent());
            int nbJoursSanction = adherent.getProfil().getRegle().getNbjoursanction();

            Sanction sanction = new Sanction();
            sanction.setAdherent(adherent);
            sanction.setDebut(today);
            sanction.setFin(today.plusDays(nbJoursSanction));
            sanctionService.save(sanction);

            return "✅ Prêt rendu avec succès. ⛔ Mais une sanction a été appliquée du " +
                    today.toLocalDate() + " au " + today.plusDays(nbJoursSanction).toLocalDate() + ".";
        }

        return "✅ Prêt rendu avec succès.";
    }

}
