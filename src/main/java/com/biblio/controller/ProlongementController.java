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

import com.biblio.model.Pret;
import com.biblio.model.Prolongement;
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
    public String validerProlongement(@RequestParam("idProlongement") Long idProlongement, Model model,
            HttpSession session) {
        Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");
        if (utilisateur == null) {
            return "redirect:/";
        }
        model.addAttribute("username", utilisateur.getNom());
        model.addAttribute("role", utilisateur.getRole());

        try {
            Prolongement prolongement = prolongementService.findById(idProlongement);

            if (prolongement == null) {
                throw new Exception("⛔ Prolongement introuvable.");
            }

            StatutProlongement statut = new StatutProlongement();
            statut.setProlongement(prolongement);
            statut.setDatemodif(LocalDateTime.now());

            statutProlongementService.saveAvecStatut(statut, 2); // 2 = Validé

            model.addAttribute("success", "✅ Prolongement validé avec succès.");
        } catch (Exception e) {
            model.addAttribute("error", "❌ Erreur : " + e.getMessage());
        }

        List<Prolongement> all = prolongementService.findProlongementEnAttente();
        model.addAttribute("prolongements", all);

        return "page/bibliothecaire/prolengement_en_attente";
    }

    @PostMapping("/refuser")
    public String refuserProlongement(@RequestParam("idProlongement") Long idProlongement, Model model,
            HttpSession session) {
        Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");
        if (utilisateur == null) {
            return "redirect:/";
        }
        model.addAttribute("username", utilisateur.getNom());
        model.addAttribute("role", utilisateur.getRole());

        try {
            Prolongement prolongement = prolongementService.findById(idProlongement);

            if (prolongement == null) {
                throw new Exception("⛔ Prolongement introuvable.");
            }

            StatutProlongement statut = new StatutProlongement();
            statut.setProlongement(prolongement);
            statut.setDatemodif(LocalDateTime.now());

            statutProlongementService.saveAvecStatut(statut, 4); // 4 = Refusé

            model.addAttribute("success", "❌ Prolongement refusé avec succès.");
        } catch (Exception e) {
            model.addAttribute("error", "❌ Erreur : " + e.getMessage());
        }

        List<Prolongement> all = prolongementService.findProlongementEnAttente();
        model.addAttribute("prolongements", all);

        return "page/bibliothecaire/prolengement_en_attente";
    }

    @PostMapping("/demande")
    public String demander(@RequestParam("idPret") Long idPret, @RequestParam("nouveaufin") LocalDate nouveaufin,
            Model model, HttpSession session) {
        Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");
        if (utilisateur == null) {
            return "redirect:/";
        }
        model.addAttribute("username", utilisateur.getNom());
        model.addAttribute("role", utilisateur.getRole());

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
    public String enattente(Model model, HttpSession session) {
        Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");
        if (utilisateur == null) {
            return "redirect:/";
        }
        model.addAttribute("username", utilisateur.getNom());
        model.addAttribute("role", utilisateur.getRole());

        List<Prolongement> all = prolongementService.findProlongementEnAttente();
        model.addAttribute("prolongements", all);

        return "page/bibliothecaire/prolengement_en_attente";
    }
}
