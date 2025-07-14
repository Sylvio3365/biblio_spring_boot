package com.biblio.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biblio.model.Adherent;
import com.biblio.model.Exemplaire;
import com.biblio.model.RegleLivre;
import com.biblio.model.Reservation;
import com.biblio.model.Statut;
import com.biblio.model.StatutReservation;
import com.biblio.repository.ReservationRepository;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private AdherentService adherentService;
    @Autowired
    private ExemplaireService exemplaireService;
    @Autowired
    private StatutReservationService statutReservationService;
    @Autowired
    private PretService pretService;

    public Reservation save(Reservation r) {
        return reservationRepository.save(r);
    }

    public List<Reservation> findAllReservationsEnAttente() {
        return reservationRepository.findAllReservationsEnAttente();
    }

    public Reservation findById(Long id) {
        return reservationRepository.findById(id).orElse(null);
    }

    public List<Reservation> findAllReservationsValider(LocalDate date) {
        return reservationRepository.findAllReservationsValider(date);
    }

    public String transformerEnPret(Long idreservation, Long idtypepret) {
        Reservation r = this.findById(idreservation);
        if (r == null) {
            return "⛔ Réservation introuvable.";
        }

        try {
            // 🟢 Tenter de créer le prêt
            String message = pretService.traiterPret(
                    r.getAdherent().getIdadherent(),
                    idtypepret,
                    r.getExemplaire().getIdexemplaire());

            // ✅ Si succès → enregistrer le nouveau statut "Transformé en prêt"
            LocalDateTime today = LocalDateTime.now().plusHours(3);
            StatutReservation sr = new StatutReservation();
            sr.setStatut(new Statut(3L, "Transformé en prêt")); // Statut ID 3
            sr.setDatemodif(today);
            sr.setReservation(r);
            statutReservationService.save(sr);

            return message;

        } catch (Exception e) {
            // ❌ Erreur lors de la tentative de prêt
            return e.getMessage(); // ou logguer e pour les logs serveur
        }
    }

    public String annulerReservation(Long idreservation) {
        LocalDateTime today = LocalDateTime.now().plusHours(3);
        Reservation r = this.findById(idreservation);

        if (r == null) {
            return "⛔ Réservation introuvable.";
        }

        StatutReservation sr = new StatutReservation();
        sr.setStatut(new Statut(4L, "Annuler")); // Statut ID 4 : Annuler
        sr.setDatemodif(today);
        sr.setReservation(r);
        statutReservationService.save(sr);

        return "❌ Réservation annulée : "
                + r.getExemplaire().getLivre().getTitre()
                + " (ex. " + r.getExemplaire().getNumero() + ") "
                + "par " + r.getAdherent().getNom() + " "
                + r.getAdherent().getPrenom() + ".";
    }

    public String validerReservation(Long idreservation) {
        LocalDateTime today = LocalDateTime.now().plusHours(3);
        Reservation r = this.findById(idreservation);

        if (r == null) {
            return "⛔ Réservation introuvable.";
        }

        StatutReservation sr = new StatutReservation();
        sr.setStatut(new Statut(2L, "Valider")); // Statut ID 2 : Valider
        sr.setDatemodif(today);
        sr.setReservation(r);
        statutReservationService.save(sr);

        return "✅ Réservation validée : "
                + r.getExemplaire().getLivre().getTitre()
                + " (ex. " + r.getExemplaire().getNumero() + ") "
                + "par " + r.getAdherent().getNom() + " "
                + r.getAdherent().getPrenom() + ".";
    }

    public String reserver(Long idUtilisateur, Long idExemplaire, LocalDate datePret) throws Exception {
        // 1️⃣ Vérifier les données d'entrée
        Adherent adherent = adherentService.findByIdutilisateur(idUtilisateur);
        Exemplaire exemplaire = exemplaireService.findById(idExemplaire);

        if (adherent == null || exemplaire == null) {
            throw new Exception("⛔ Données invalides – veuillez vérifier vos sélections.");
        }

        // 2️⃣ Vérifier le quota de réservations en attente
        int quota = adherent.getProfil().getQuota().getReservation();
        int nbReservationsEnAttente = adherentService.countReservationEnAttente(adherent.getIdadherent());

        if (nbReservationsEnAttente >= quota) {
            throw new Exception("⛔ Quota de réservation atteint (" + quota + " en attente non validées).");
        }

        // 3️⃣ Vérifier si l’adhérent est actif, abonné et non sanctionné
        String messageAdherent = adherentService.checkAdherent(adherent.getIdadherent());
        if (!messageAdherent.contains("✅")) {
            throw new Exception(messageAdherent);
        }

        // 4️⃣ Vérifier la disponibilité de l'exemplaire
        if (!exemplaireService.estDisponible(idExemplaire)) {
            throw new Exception("⛔ L'exemplaire sélectionné n'est pas disponible.");
        }

        // 5️⃣ Vérifier l'âge minimum requis si défini dans la règle du livre
        RegleLivre regleLivre = exemplaire.getLivre().getRegleLivre();
        if (regleLivre != null) {
            int age = adherent.getAge();
            int ageMin = regleLivre.getAgemin();
            if (age < ageMin) {
                throw new Exception("⛔ Âge requis : " + ageMin + " ans – Âge actuel : " + age + " ans.");
            }
        }

        // 6️⃣ Création de la réservation
        LocalDateTime today = LocalDateTime.now().plusHours(3);

        Reservation r = new Reservation();
        r.setAdherent(adherent);
        r.setDatepret(datePret);
        r.setDatereservation(today);
        r.setExemplaire(exemplaire);
        this.save(r);

        StatutReservation sr = new StatutReservation();
        sr.setStatut(new Statut(1L, "En attente"));
        sr.setDatemodif(today);
        sr.setReservation(r);
        statutReservationService.save(sr);

        // ✅ Succès
        return "✅ Réservation autorisée – vous pouvez la finaliser.";
    }

}
