package com.biblio.service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biblio.model.Adherent;
import com.biblio.model.Etat;
import com.biblio.model.EtatExemplaire;
import com.biblio.model.Pret;
import com.biblio.model.Prolongement;
import com.biblio.model.RegleJF;
import com.biblio.model.Rendre;
import com.biblio.model.Sanction;
import com.biblio.model.StatutProlongement;
import com.biblio.model.utils.Ilaina;
import com.biblio.repository.RendreRepository;

@Service
public class RendreService {

    @Autowired
    private RendreRepository rendreRepository;
    @Autowired
    private AdherentService adherentService;
    @Autowired
    private SanctionService sanctionService;
    @Autowired
    private EtatExemplaireService etatExemplaireService;
    @Autowired
    private RegleJFService regleJFService;
    @Autowired
    private JourFerieService jourFerieService;
    @Autowired
    private ProlongementService prolongementService;
    @Autowired
    private StatutProlongementService statutProlongementService;

    public Rendre save(Rendre r) {
        return rendreRepository.save(r);
    }

    public String rendrePretAvecDate(Pret pret, LocalDate dateretour) {
        LocalDateTime now = dateretour.atTime(02, 00, 00); // on considère que le rendu est fait en fin de journée
        LocalDateTime dateFin = pret.getFin();
        LocalDate dateRetour = now.toLocalDate();

        // 🔍 Vérifier prolongement validé
        Prolongement prolongement = prolongementService.findByIdPret(pret.getIdpret());
        if (prolongement != null) {
            StatutProlongement st = statutProlongementService
                    .findDernierStatutByIdProlongement(prolongement.getIdprolongement());
            if (st != null && st.getStatut().getIdstatut().equals(2L)) {
                dateFin = prolongement.getNouveaufin();
            }
        }

        if (dateRetour.isBefore(pret.getDebut().toLocalDate())) {
            return "⛔ Rendu impossible : la date de retour est antérieure à la date de début du prêt.";
        }

        LocalDate dateFinSansHeure = dateFin.toLocalDate();

        // ❌ 0. Si la date de retour est un jour non ouvrable → refus
        if (jourFerieService.isJourFerie(dateRetour) || Ilaina.isSamedi(dateRetour) || Ilaina.isDimanche(dateRetour)) {
            return "⛔ Rendu impossible : la date choisie est un jour non ouvrable (jour férié, samedi ou dimanche).";
        }

        // ✅ 1. Si la date de fin est un jour ouvrable → ne pas appliquer les règles JF
        boolean dateFinOuvrable = !jourFerieService.isJourFerie(dateFinSansHeure)
                && !Ilaina.isSamedi(dateFinSansHeure)
                && !Ilaina.isDimanche(dateFinSansHeure);

        if (!dateFinOuvrable) {
            // 🔁 2. Récupérer la dernière règle JF
            RegleJF regleJF = regleJFService.getDerniereRegle();

            // 🕒 3. Comportement AVANT
            if (regleJF.getComportement() == 0) {
                LocalDate jourPermis = dateFinSansHeure;
                while (jourFerieService.isJourFerie(jourPermis)
                        || Ilaina.isSamedi(jourPermis)
                        || Ilaina.isDimanche(jourPermis)) {
                    jourPermis = jourPermis.minusDays(1);
                }

                if (dateRetour.equals(jourPermis)) {
                    enregistrerRendu(pret, now);
                    return "✅ Prêt rendu avec succès (rendu le jour ouvrable précédent autorisé).";
                }
            }

            // 🕒 4. Comportement APRÈS
            if (regleJF.getComportement() == 1) {
                LocalDate jourRetour = dateFinSansHeure;
                while (jourFerieService.isJourFerie(jourRetour)
                        || Ilaina.isSamedi(jourRetour)
                        || Ilaina.isDimanche(jourRetour)) {
                    jourRetour = jourRetour.plusDays(1);
                }

                if (dateRetour.equals(jourRetour)) {
                    enregistrerRendu(pret, now);
                    return "✅ Prêt rendu avec succès (rendu le jour ouvrable suivant autorisé).";
                }
            }
        }

        // ✅ 5. Rendu normal dans les délais
        if (!now.isAfter(dateFin)) {
            enregistrerRendu(pret, now);
            return "✅ Prêt rendu avec succès dans les délais.";
        }

        // ⛔ 6. Retard ➤ sanction
        Adherent adherent = adherentService.findById(pret.getAdherent().getIdadherent());
        int nbJoursSanction = adherent.getProfil().getRegle().getNbjoursanction();

        enregistrerRendu(pret, now);

        Sanction sanction = new Sanction();
        sanction.setAdherent(adherent);
        sanction.setDebut(now);
        sanction.setFin(now.plusDays(nbJoursSanction));
        sanctionService.save(sanction);

        return "✅ Prêt rendu, mais hors délai conforme. ⛔ Sanction appliquée du " +
                now.toLocalDate() + " au " + now.plusDays(nbJoursSanction).toLocalDate() + ".";
    }

    public String rendrePret(Pret pret) {
        LocalDateTime now = LocalDateTime.now().plusHours(3);
        LocalDate today = now.toLocalDate();
        LocalDateTime dateFin = pret.getFin();

        // 🔍 Vérifier prolongement validé
        Prolongement prolongement = prolongementService.findByIdPret(pret.getIdpret());
        if (prolongement != null) {
            StatutProlongement st = statutProlongementService
                    .findDernierStatutByIdProlongement(prolongement.getIdprolongement());
            if (st != null && st.getStatut().getIdstatut().equals(2L)) {
                dateFin = prolongement.getNouveaufin();
            }
        }

        LocalDate dateFinSansHeure = dateFin.toLocalDate();

        // ❌ 0. Si aujourd’hui est un jour non ouvrable → refus
        if (jourFerieService.isJourFerie(today) || Ilaina.isSamedi(today) || Ilaina.isDimanche(today)) {
            return "⛔ Rendu impossible : aujourd'hui est un jour non ouvrable (jour férié, samedi ou dimanche).";
        }

        // ✅ 1. Si la date de fin est un jour ouvrable → ne pas appliquer les règles JF
        boolean dateFinOuvrable = !jourFerieService.isJourFerie(dateFinSansHeure)
                && !Ilaina.isSamedi(dateFinSansHeure)
                && !Ilaina.isDimanche(dateFinSansHeure);

        if (!dateFinOuvrable) {
            // 🔁 2. Récupérer la dernière règle JF
            RegleJF regleJF = regleJFService.getDerniereRegle();

            // 🕒 3. Comportement AVANT
            if (regleJF.getComportement() == 0) {
                LocalDate jourPermis = dateFinSansHeure;
                while (jourFerieService.isJourFerie(jourPermis)
                        || Ilaina.isSamedi(jourPermis)
                        || Ilaina.isDimanche(jourPermis)) {
                    jourPermis = jourPermis.minusDays(1);
                }

                if (today.equals(jourPermis)) {
                    enregistrerRendu(pret, now);
                    return "✅ Prêt rendu avec succès (rendu le jour ouvrable précédent autorisé).";
                }
            }

            // 🕒 4. Comportement APRÈS
            if (regleJF.getComportement() == 1) {
                LocalDate jourRetour = dateFinSansHeure;
                while (jourFerieService.isJourFerie(jourRetour)
                        || Ilaina.isSamedi(jourRetour)
                        || Ilaina.isDimanche(jourRetour)) {
                    jourRetour = jourRetour.plusDays(1);
                }

                if (today.equals(jourRetour)) {
                    enregistrerRendu(pret, now);
                    return "✅ Prêt rendu avec succès (rendu le jour ouvrable suivant autorisé).";
                }
            }
        }

        // ✅ 5. Rendu normal dans les délais
        if (!now.isAfter(dateFin)) {
            enregistrerRendu(pret, now);
            return "✅ Prêt rendu avec succès dans les délais.";
        }

        // ⛔ 6. Retard ➤ sanction
        Adherent adherent = adherentService.findById(pret.getAdherent().getIdadherent());
        int nbJoursSanction = adherent.getProfil().getRegle().getNbjoursanction();

        enregistrerRendu(pret, now);

        Sanction sanction = new Sanction();
        sanction.setAdherent(adherent);
        sanction.setDebut(now);
        sanction.setFin(now.plusDays(nbJoursSanction));
        sanctionService.save(sanction);

        return "✅ Prêt rendu, mais hors délai conforme. ⛔ Sanction appliquée du " +
                now.toLocalDate() + " au " + now.plusDays(nbJoursSanction).toLocalDate() + ".";
    }

    private void enregistrerRendu(Pret pret, LocalDateTime now) {
        Rendre rendu = new Rendre();
        rendu.setPret(pret);
        rendu.setDaterendu(now);
        rendreRepository.save(rendu);

        EtatExemplaire ee = new EtatExemplaire();
        ee.setDateheure(now);
        ee.setEtat(new Etat(1L, "Disponible"));
        ee.setExemplaire(pret.getExemplaire());
        etatExemplaireService.save(ee);
    }

}
