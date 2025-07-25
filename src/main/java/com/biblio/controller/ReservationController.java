package com.biblio.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.biblio.model.Exemplaire;
import com.biblio.model.Reservation;
import com.biblio.model.TypePret;
import com.biblio.model.Utilisateur;
import com.biblio.service.ExemplaireService;
import com.biblio.service.ReservationService;
import com.biblio.service.TypePretService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/reservation")
public class ReservationController {

    @Autowired
    private ExemplaireService exemplaireService;
    @Autowired
    private ReservationService reservationService;
    @Autowired
    private TypePretService typePretService;

    @GetMapping("/reserver")
    public String formReservation(Model model, HttpSession session) {
        Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");
        if (utilisateur == null) {
            return "redirect:/";
        }

        model.addAttribute("username", utilisateur.getNom());
        model.addAttribute("role", utilisateur.getRole());

        List<Exemplaire> exemplaires = exemplaireService.findAll();
        if (exemplaires == null || exemplaires.isEmpty()) {
            model.addAttribute("error", "Aucun exemplaire disponible pour réservation.");
        } else {
            model.addAttribute("exemplaires", exemplaires);
        }

        return "page/adherent/reservation";
    }

    @PostMapping("/save")
    public String saveReservation(
            @RequestParam("idExemplaire") Long idExemplaire,
            @RequestParam("datepret") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate datePret,
            HttpSession session,
            Model model) {

        Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");
        if (utilisateur == null) {
            return "redirect:/";
        }

        model.addAttribute("username", utilisateur.getNom());
        model.addAttribute("role", utilisateur.getRole());

        try {
            String message = reservationService.reserver(utilisateur.getIdutilisateur(), idExemplaire, datePret);
            model.addAttribute("success", message);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }

        model.addAttribute("exemplaires", exemplaireService.findAll());
        return "page/adherent/reservation";
    }

    @GetMapping("/en_attente")
    public String voirReservationEnAttente(Model model, HttpSession session) {
        Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");
        if (utilisateur == null)
            return "redirect:/";

        model.addAttribute("username", utilisateur.getNom());
        model.addAttribute("role", utilisateur.getRole());

        List<Reservation> alls = reservationService.findAllReservationsEnAttente();
        model.addAttribute("reservations", alls);
        return "page/bibliothecaire/reservation_en_attente";
    }

    @PostMapping("/valider")
    public String validerReservation(@RequestParam Long idReservation, Model model, HttpSession session) {
        Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");
        if (utilisateur == null)
            return "redirect:/";

        model.addAttribute("username", utilisateur.getNom());
        model.addAttribute("role", utilisateur.getRole());

        String message = reservationService.validerReservation(idReservation);

        List<Reservation> alls = reservationService.findAllReservationsEnAttente();
        model.addAttribute("reservations", alls);
        model.addAttribute("success", message);
        return "page/bibliothecaire/reservation_en_attente";
    }

    @PostMapping("/annuler")
    public String annulerReservation(@RequestParam Long idReservation, Model model, HttpSession session) {
        Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");
        if (utilisateur == null)
            return "redirect:/";

        model.addAttribute("username", utilisateur.getNom());
        model.addAttribute("role", utilisateur.getRole());

        String message = reservationService.annulerReservation(idReservation);

        List<Reservation> alls = reservationService.findAllReservationsEnAttente();
        model.addAttribute("reservations", alls);
        model.addAttribute("error", message);
        return "page/bibliothecaire/reservation_en_attente";
    }

    @GetMapping("/en_pret")
    public String showReservationValidee(
            @RequestParam(name = "datepret", required = false) LocalDate datepret,
            Model model,
            HttpSession session) {

        Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");
        if (utilisateur == null)
            return "redirect:/";

        model.addAttribute("username", utilisateur.getNom());
        model.addAttribute("role", utilisateur.getRole());

        if (datepret == null) {
            datepret = LocalDate.now();
        }

        List<Reservation> reservations = reservationService.findAllReservationsValider(datepret);
        List<TypePret> typepret = typePretService.findAll();
        model.addAttribute("reservations", reservations);
        model.addAttribute("datepret", datepret);
        model.addAttribute("typepret", typepret);
        return "page/bibliothecaire/reservation_en_pret";
    }

    @PostMapping("/transformer")
    public String transformerEnPret(
            @RequestParam("idReservation") Long idReservation,
            @RequestParam("idTypePret") Long idTypePret,
            Model model,
            HttpSession session) {

        Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");
        if (utilisateur == null)
            return "redirect:/";

        model.addAttribute("username", utilisateur.getNom());
        model.addAttribute("role", utilisateur.getRole());

        String resultat = reservationService.transformerEnPret(idReservation, idTypePret);

        if (!resultat.startsWith("✅")) {
            model.addAttribute("error", resultat);
        } else {
            model.addAttribute("success", resultat);
        }

        LocalDate datePret = reservationService.findById(idReservation).getDatepret();
        List<Reservation> reservations = reservationService.findAllReservationsValider(datePret);
        List<TypePret> typepret = typePretService.findAll();
        model.addAttribute("reservations", reservations);
        model.addAttribute("datepret", datePret);
        model.addAttribute("typepret", typepret);

        return "page/bibliothecaire/reservation_en_pret";
    }

    @PostMapping("/refuser")
    public String annulerReservationTenaIzy(@RequestParam Long idReservation, Model model, HttpSession session) {
        Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");
        if (utilisateur == null)
            return "redirect:/";

        model.addAttribute("username", utilisateur.getNom());
        model.addAttribute("role", utilisateur.getRole());

        String message = reservationService.annulerReservation(idReservation);

        if (!message.startsWith("✅")) {
            model.addAttribute("error", message);
        } else {
            model.addAttribute("success", message);
        }

        LocalDate datePret = reservationService.findById(idReservation).getDatepret();
        List<Reservation> reservations = reservationService.findAllReservationsValider(datePret);
        List<TypePret> typepret = typePretService.findAll();

        model.addAttribute("reservations", reservations);
        model.addAttribute("datepret", datePret);
        model.addAttribute("typepret", typepret);

        return "page/bibliothecaire/reservation_en_pret";
    }
}
