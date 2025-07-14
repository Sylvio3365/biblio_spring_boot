package com.biblio.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.biblio.model.Pret;
import com.biblio.model.Prolongement;
import com.biblio.model.Statut;
import com.biblio.model.StatutProlongement;
import com.biblio.model.Utilisateur;
import com.biblio.model.utils.PretAvecProlongementDTO;
import com.biblio.service.PretService;
import com.biblio.service.ProlongementService;
import com.biblio.service.StatutProlongementService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/prolongement")
public class ProlongementController {

    @Autowired
    private PretService pretService;
    @Autowired
    private ProlongementService prolongementService;
    @Autowired
    private StatutProlongementService statutProlongementService;

    @PostMapping("/valider")
    public String validerProlongement(@RequestParam("idProlongement") Long idProlongement,
            Model m) {
        try {
            Prolongement prolongement = prolongementService.findById(idProlongement);

            if (prolongement == null) {
                throw new Exception("⛔ Prolongement introuvable.");
            }

            StatutProlongement statut = new StatutProlongement();
            statut.setProlongement(prolongement);
            statut.setDatemodif(LocalDateTime.now());

            statutProlongementService.saveAvecStatut(statut, 2); // 2 = Validé

            m.addAttribute("success", "✅ Prolongement validé avec succès.");
        } catch (Exception e) {
            m.addAttribute("error", "❌ Erreur : " + e.getMessage());
        }

        // ✅ NÉCESSAIRE : toujours renvoyer la liste à la vue
        List<Prolongement> all = prolongementService.findProlongementEnAttente();
        m.addAttribute("prolongements", all);

        return "page/bibliothecaire/prolengement_en_attente";
    }

    @PostMapping("/refuser")
    public String refuserProlongement(@RequestParam("idProlongement") Long idProlongement,
            Model m) {
        try {
            Prolongement prolongement = prolongementService.findById(idProlongement);

            if (prolongement == null) {
                throw new Exception("⛔ Prolongement introuvable.");
            }

            StatutProlongement statut = new StatutProlongement();
            statut.setProlongement(prolongement);
            statut.setDatemodif(LocalDateTime.now());

            statutProlongementService.saveAvecStatut(statut, 4); // 4 = Refusé

            m.addAttribute("success", "❌ Prolongement refusé avec succès.");
        } catch (Exception e) {
            m.addAttribute("error", "❌ Erreur : " + e.getMessage());
        }

        // ✅ Recharge la liste
        List<Prolongement> all = prolongementService.findProlongementEnAttente();
        m.addAttribute("prolongements", all);

        return "page/bibliothecaire/prolengement_en_attente";
    }

    @PostMapping("/demande")
    public String demander(@RequestParam("idPret") Long idPret, @RequestParam("nouveaufin") LocalDate nouveaufin,
            Model model, HttpSession session) {

        Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");

        if (utilisateur == null) {
            return "redirect:/";
        }
        Pret p = pretService.findById(idPret);
        try {
            prolongementService.demandeProlongationPret(p, nouveaufin.atTime(23, 59));
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        try {
            List<PretAvecProlongementDTO> mesprets = pretService
                    .getPretNonRenduAvecProlongement(utilisateur.getIdutilisateur());
            model.addAttribute("mesprets", mesprets);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "page/adherent/mes_prets";
    }

    @GetMapping("/en_attente")
    public String enattente(Model model) {
        List<Prolongement> all = prolongementService.findProlongementEnAttente();
        model.addAttribute("prolongements", all);
        return "page/bibliothecaire/prolengement_en_attente";
    }
}
