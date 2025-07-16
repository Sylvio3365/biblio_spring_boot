package com.biblio.service;

import com.biblio.model.Adherent;
import com.biblio.model.Etat;
import com.biblio.model.EtatExemplaire;
import com.biblio.model.Exemplaire;
import com.biblio.model.Pret;
import com.biblio.model.Prolongement;
import com.biblio.model.RegleLivre;
import com.biblio.model.StatutProlongement;
import com.biblio.model.TypePret;
import com.biblio.model.utils.PretAvecProlongementDTO;
import com.biblio.repository.AdherentRepository;
import com.biblio.repository.PretRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    @Autowired
    private ProlongementService prolongementService;
    @Autowired
    private StatutProlongementService statutProlongementService;
    @Autowired
    private AdherentRepository adherentRepository;

    public Pret save(Pret pret) {
        return pretRepository.save(pret);
    }

    public List<Pret> getPretNonRendu() {
        return pretRepository.getPretNonRendu();
    }

    public List<PretAvecProlongementDTO> getPretNonRenduAvecProlongement(Long idutilisateur) throws Exception {
        Adherent a = adherentService.findByIdutilisateur(idutilisateur);
        if (a == null) {
            throw new Exception("⛔ Adhérent non trouvé.");
        }

        Long idadherent = a.getIdadherent();
        List<Pret> prets = pretRepository.getPretNonRenduByIdAdherent(idadherent);

        List<PretAvecProlongementDTO> resultat = new ArrayList<>();

        for (Pret p : prets) {
            Prolongement prolongement = prolongementService.findByIdPret(p.getIdpret());
            StatutProlongement statut = null;

            if (prolongement != null) {
                statut = statutProlongementService.findDernierStatutByIdProlongement(prolongement.getIdprolongement());
            }

            PretAvecProlongementDTO dto = new PretAvecProlongementDTO(p, prolongement, statut);
            resultat.add(dto);
        }

        return resultat;
    }

    public List<Pret> getPretNonRenduByIdAdherent(Long idutilisateur) throws Exception {
        Adherent a = adherentService.findByIdutilisateur(idutilisateur);
        if (a == null) {
            throw new Exception("Adherent non trouvee");
        }
        Long idadherent = a.getIdadherent();
        return pretRepository.getPretNonRenduByIdAdherent(idadherent);
    }

    public Pret findById(Long id) {
        return pretRepository.findById(id).orElse(null);
    }

    public String traiterPret(Long idAdherent, Long idTypePret, Long idExemplaire) throws Exception {

        // 1️⃣ Vérification des entités de base
        Adherent adherent = adherentService.findById(idAdherent);
        TypePret typepret = typePretService.findById(idTypePret);
        Exemplaire exemplaire = exemplaireService.findById(idExemplaire);

        if (adherent == null || typepret == null || exemplaire == null) {
            throw new Exception("⛔ Données invalides – vérifiez les sélections.");
        }

        // 2️⃣ Vérifier le quota de prêt
        if (idTypePret == 1L) {
            // 2️⃣ Vérifier le quota de prêt
            int quotaPret = adherent.getProfil().getQuota().getPret();

            // int nbPretNonRendu = adherentService.getNbPretNonRendu(idAdherent);

            int nbPretNonRendu = adherentRepository.getNbPretNonRenduAdomicile(idAdherent);
            if (nbPretNonRendu >= quotaPret) {
                throw new Exception("⛔ Quota de prêt atteint (" + quotaPret + " en cours non rendus).");
            }
        }

        // 3️⃣ Vérifier les conditions liées à l’adhérent
        adherentService.checkAdherent(idAdherent); // Cette méthode jette déjà les exceptions nécessaires

        // 4️⃣ Vérifier que l’exemplaire est disponible
        if (!exemplaireService.estDisponible(idExemplaire)) {
            throw new Exception("⛔ L'exemplaire sélectionné est déjà en prêt.");
        }

        // 5️⃣ Vérifier la correspondance type de prêt / exemplaire
        if (!exemplaire.getTypePret().getIdtypepret().equals(typepret.getIdtypepret())) {
            throw new Exception("⛔ Type de prêt non conforme à l'exemplaire sélectionné.");
        }

        // 6️⃣ Vérifier l'âge de l’adhérent
        RegleLivre regleLivre = exemplaire.getLivre().getRegleLivre();
        if (regleLivre != null) {
            int ageAdherent = adherent.getAge();
            int ageMin = regleLivre.getAgemin();
            if (ageAdherent < ageMin) {
                throw new Exception("⛔ Âge requis : " + ageMin + " ans. Âge actuel : " + ageAdherent + " ans.");
            }
        }

        // 7️⃣ Créer le prêt
        LocalDateTime debut = LocalDateTime.now().plusHours(3);
        LocalDateTime fin = debut.plusDays(adherent.getProfil().getRegle().getNbjourpret());
        if (idTypePret == 2L) {
            fin = debut.withHour(23).withMinute(59).withSecond(59);
        }
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

        return "✅ Prêt enregistré avec succès.";
    }

    public String traiterPretAvecDate(Long idAdherent, Long idTypePret, Long idExemplaire, LocalDate datePret)
            throws Exception {

        // 1️⃣ Vérification des entités de base
        Adherent adherent = adherentService.findById(idAdherent);
        TypePret typepret = typePretService.findById(idTypePret);
        Exemplaire exemplaire = exemplaireService.findById(idExemplaire);

        if (adherent == null || typepret == null || exemplaire == null) {
            throw new Exception("⛔ Données invalides – vérifiez les sélections.");
        }

        if (idTypePret == 1L) {
            // 2️⃣ Vérifier le quota de prêt
            int quotaPret = adherent.getProfil().getQuota().getPret();

            // int nbPretNonRendu = adherentService.getNbPretNonRendu(idAdherent);

            int nbPretNonRendu = adherentRepository.getNbPretNonRenduAdomicile(idAdherent);
            if (nbPretNonRendu >= quotaPret) {
                throw new Exception("⛔ Quota de prêt atteint (" + quotaPret + " en cours non rendus).");
            }
        }

        // 3️⃣ Vérifier les conditions liées à l’adhérent
        adherentService.checkAdherentWithDate(idAdherent, datePret); // Cette méthode jette déjà les exceptions
                                                                     // nécessaires

        // 4️⃣ Vérifier que l’exemplaire est disponible
        if (!exemplaireService.estDisponible(idExemplaire)) {
            throw new Exception("⛔ L'exemplaire sélectionné est déjà en prêt.");
        }

        // 5️⃣ Vérifier la correspondance type de prêt / exemplaire
        if (!exemplaire.getTypePret().getIdtypepret().equals(typepret.getIdtypepret())) {
            throw new Exception("⛔ Type de prêt non conforme à l'exemplaire sélectionné.");
        }

        // 6️⃣ Vérifier l'âge de l’adhérent
        RegleLivre regleLivre = exemplaire.getLivre().getRegleLivre();
        if (regleLivre != null) {
            int ageAdherent = adherent.getAge();
            int ageMin = regleLivre.getAgemin();
            if (ageAdherent < ageMin) {
                throw new Exception("⛔ Âge requis : " + ageMin + " ans. Âge actuel : " + ageAdherent + " ans.");
            }
        }

        // 7️⃣ Créer le prêt
        LocalDateTime debut = datePret.atStartOfDay().plusHours(2);
        LocalDateTime fin = debut.plusDays(adherent.getProfil().getRegle().getNbjourpret());

        if (idTypePret == 2L) {
            fin = debut.withHour(23).withMinute(59).withSecond(59);
        }

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

        return "✅ Prêt enregistré avec succès.";
    }

}
