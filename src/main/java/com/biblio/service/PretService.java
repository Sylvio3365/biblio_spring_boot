package com.biblio.service;

import com.biblio.model.Adherent;
import com.biblio.model.Etat;
import com.biblio.model.EtatExemplaire;
import com.biblio.model.Exemplaire;
import com.biblio.model.Pret;
import com.biblio.model.RegleLivre;
import com.biblio.model.TypePret;
import com.biblio.repository.PretRepository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PretService {

    @Autowired
    private PretRepository pretRepository;
    @Autowired
    private AdherentService adherentService;
    @Autowired
    private TypePretService typePretService;
    @Autowired
    private ExemplaireService exemplaireService;
    @Autowired
    private EtatExemplaireService etatExemplaireService;

    public Pret save(Pret pret) {
        return pretRepository.save(pret);
    }

    public List<Pret> getPretNonRendu() {
        return pretRepository.getPretNonRendu();
    }

    public String traiterPret(Long idAdherent, Long idTypePret, Long idExemplaire) {

        // 1️⃣ Vérification des entités de base
        Adherent adherent = adherentService.findById(idAdherent);
        TypePret typepret = typePretService.findById(idTypePret);
        Exemplaire exemplaire = exemplaireService.findById(idExemplaire);

        if (adherent == null || typepret == null || exemplaire == null) {
            return "⛔ Données invalides – vérifiez les sélections.";
        }

        // 2️⃣ Vérifier le quota de prêt AVANT TOUT (nombre de prêts en cours non
        // rendus)
        int quotaPret = adherent.getProfil().getQuota().getPret();
        int nbPretNonRendu = adherentService.getNbPretNonRendu(idAdherent);
        if (nbPretNonRendu >= quotaPret) {
            return "⛔ Quota de prêt atteint (" + quotaPret + " en cours non rendus).";
        }

        // 3️⃣ Vérifier les conditions liées à l’adhérent : actif, abonné, non
        // sanctionné
        String message = adherentService.checkAdherent(idAdherent);
        if (!message.contains("✅")) {
            return message;
        }

        // 4️⃣ Vérifier que l’exemplaire est disponible
        if (!exemplaireService.estDisponible(idExemplaire)) {
            return "⛔ L'exemplaire sélectionné est déjà en prêt.";
        }

        // 5️⃣ Vérifier la correspondance entre type de prêt et l’exemplaire
        if (!exemplaire.getTypePret().getIdtypepret().equals(typepret.getIdtypepret())) {
            return "⛔ Type de prêt non conforme à l'exemplaire sélectionné.";
        }

        // 6️⃣ Vérifier si l’âge de l’adhérent respecte la règle d’âge du livre
        RegleLivre regleLivre = exemplaire.getLivre().getRegleLivre();
        if (regleLivre != null) {
            int ageAdherent = adherent.getAge();
            int ageMin = regleLivre.getAgemin();
            if (ageAdherent < ageMin) {
                return "⛔ Âge requis : " + ageMin + " ans. Âge actuel : " + ageAdherent + " ans.";
            }
        }

        // 7️⃣ Tout est bon : créer le prêt
        LocalDateTime debut = LocalDateTime.now().plusHours(3); // Heure locale sans ZoneId
        LocalDateTime fin = debut.plusDays(quotaPret);

        Pret pret = new Pret();
        pret.setAdherent(adherent);
        pret.setExemplaire(exemplaire);
        pret.setTypepret(typepret);
        pret.setDebut(debut);
        pret.setFin(fin);
        pretRepository.save(pret);

        // 8️⃣ Mettre à jour l’état de l’exemplaire (En prêt)
        EtatExemplaire ee = new EtatExemplaire();
        ee.setDateheure(debut);
        ee.setEtat(new Etat(2L, "En pret"));
        ee.setExemplaire(exemplaire);
        etatExemplaireService.save(ee);

        // ✅ Fin
        return "✅ Prêt enregistré avec succès.";
    }

}
