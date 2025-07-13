package com.biblio.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.biblio.model.Adherent;
import com.biblio.model.Exemplaire;
import com.biblio.model.RegleLivre;
import com.biblio.model.TypePret;
import com.biblio.service.AdherentService;
import com.biblio.service.ExemplaireService;
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

        model.addAttribute("adherents", adherentService.findAll());
        model.addAttribute("typeprets", typePretService.findAll());
        model.addAttribute("exemplaires", exemplaireService.findAll());

        String message = adherentService.checkAdherent(idAdherent);
        if (!message.contains("✅")) {
            model.addAttribute("error", message);
            return "page/bibliothecaire/pret";
        }

        boolean estDisponible = exemplaireService.estDisponible(idExemplaire);
        if (!estDisponible) {
            model.addAttribute("error", "⛔ L'exemplaire sélectionné est déjà en prêt.");
            return "page/bibliothecaire/pret";
        }

        TypePret typepret = typePretService.findById(idTypePret);
        Exemplaire exemplaire = exemplaireService.findById(idExemplaire);
        Adherent adherent = adherentService.findById(idAdherent);

        if (exemplaire == null) {
            model.addAttribute("error", "⛔ L'exemplaire sélectionné est introuvable.");
            return "page/bibliothecaire/pret";
        }

        if (!exemplaire.getTypePret().getIdtypepret().equals(typepret.getIdtypepret())) {
            model.addAttribute("error", "⛔ Type de prêt non conforme à l'exemplaire sélectionné.");
            return "page/bibliothecaire/pret";
        }

        RegleLivre regleLivre = exemplaire.getLivre().getRegleLivre();
        if (regleLivre != null) {
            int ageAdherent = adherent.getAge();
            int ageMin = regleLivre.getAgemin();

            if (ageAdherent < ageMin) {
                model.addAttribute("error",
                        "⛔ Âge insuffisant pour emprunter ce livre (âge requis : " + ageMin + " ans).");
                return "page/bibliothecaire/pret";
            }
        }

        // ✅ Ici, vous pouvez maintenant appeler votre service pour enregistrer le prêt
        // pretService.save(...);

        model.addAttribute("success", "✅ Prêt autorisé – vous pouvez l’enregistrer.");
        return "page/bibliothecaire/pret";
    }

}
