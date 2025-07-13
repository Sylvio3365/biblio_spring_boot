package com.biblio.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.biblio.model.Adherent;
import com.biblio.model.Etat;
import com.biblio.model.EtatExemplaire;
import com.biblio.model.Exemplaire;
import com.biblio.model.Pret;
import com.biblio.model.Quota;
import com.biblio.model.RegleLivre;
import com.biblio.model.TypePret;
import com.biblio.service.AdherentService;
import com.biblio.service.EtatExemplaireService;
import com.biblio.service.ExemplaireService;
import com.biblio.service.PretService;
import com.biblio.service.TypePretService;

@Controller
@RequestMapping("/pret")
public class PretController {

    @Autowired
    private AdherentService adherentService;

    @Autowired
    private TypePretService typePretService;

    @Autowired
    private ExemplaireService exemplaireService;

    @Autowired
    private PretService pretService;

    @Autowired
    private EtatExemplaireService etatExemplaireService;

    @GetMapping("/form")
    public String newPret(Model model) {
        List<Adherent> adherents = adherentService.findAll();
        List<TypePret> typeprets = typePretService.findAll();
        List<Exemplaire> exemplaires = exemplaireService.findAll();
        model.addAttribute("adherents", adherents);
        model.addAttribute("typeprets", typeprets);
        model.addAttribute("exemplaires", exemplaires);
        return "page/bibliothecaire/pret";
    }

    @PostMapping("/save")
    public String savePret(
            @RequestParam("idAdherent") Long idAdherent,
            @RequestParam("idTypePret") Long idTypePret,
            @RequestParam("idExemplaire") Long idExemplaire,
            Model model) {

        // 🔁 Charger les listes pour la réaffichage du formulaire
        model.addAttribute("adherents", adherentService.findAll());
        model.addAttribute("typeprets", typePretService.findAll());
        model.addAttribute("exemplaires", exemplaireService.findAll());

        // ✅ Vérification de l’adhérent (actif, abonné, non sanctionné)
        String message = adherentService.checkAdherent(idAdherent);
        if (!message.contains("✅")) {
            model.addAttribute("error", message);
            return "page/bibliothecaire/pret";
        }

        // ✅ Vérification disponibilité de l'exemplaire
        if (!exemplaireService.estDisponible(idExemplaire)) {
            model.addAttribute("error", "⛔ L'exemplaire sélectionné est déjà en prêt.");
            return "page/bibliothecaire/pret";
        }

        // 🔄 Récupération des entités
        TypePret typepret = typePretService.findById(idTypePret);
        Exemplaire exemplaire = exemplaireService.findById(idExemplaire);
        Adherent adherent = adherentService.findById(idAdherent);

        if (exemplaire == null || typepret == null || adherent == null) {
            model.addAttribute("error", "⛔ Données invalides – vérifiez les sélections.");
            return "page/bibliothecaire/pret";
        }

        // ✅ Vérification du type de prêt
        if (!exemplaire.getTypePret().getIdtypepret().equals(typepret.getIdtypepret())) {
            model.addAttribute("error", "⛔ Type de prêt non conforme à l'exemplaire sélectionné.");
            return "page/bibliothecaire/pret";
        }

        // ✅ Vérification d'âge si règle d'âge définie
        RegleLivre regleLivre = exemplaire.getLivre().getRegleLivre();
        if (regleLivre != null) {
            int ageAdherent = adherent.getAge();
            int ageMin = regleLivre.getAgemin();

            if (ageAdherent < ageMin) {
                model.addAttribute("error", "⛔ Âge requis : " + ageMin + " ans. Âge actuel : " + ageAdherent + " ans.");
                return "page/bibliothecaire/pret";
            }
        }

        // ✅ Vérification du quota de prêts non rendus
        int nbPretNonRendu = adherentService.getNbPretNonRendu(idAdherent);
        int quotaPret = adherent.getProfil().getQuota().getPret();
        if (nbPretNonRendu >= quotaPret) {
            model.addAttribute("error", "⛔ Quota de prêt atteint (" + quotaPret + " en cours non rendus).");
            return "page/bibliothecaire/pret";
        }

        // ✅ Création et enregistrement du prêt
        LocalDateTime debut = LocalDateTime.now().plusHours(3); // GMT+3 sans utiliser ZoneId
        LocalDateTime fin = debut.plusDays(quotaPret);

        Pret pret = new Pret();
        pret.setAdherent(adherent);
        pret.setExemplaire(exemplaire);
        pret.setTypepret(typepret);
        pret.setDebut(debut);
        pret.setFin(fin);
        pretService.save(pret);

        // 🔄 Mise à jour de l'état de l'exemplaire (En prêt)
        EtatExemplaire ee = new EtatExemplaire();
        ee.setDateheure(debut);
        ee.setEtat(new Etat(2L, "En pret"));
        ee.setExemplaire(exemplaire);
        etatExemplaireService.save(ee);

        // ✅ Succès
        model.addAttribute("success", "✅ Prêt enregistré avec succès.");
        return "page/bibliothecaire/pret";
    }

}
