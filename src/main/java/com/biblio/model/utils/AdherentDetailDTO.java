package com.biblio.model.utils;

import com.biblio.model.Adherent;

public class AdherentDetailDTO {
    // sanction
    // abonnement
    // quota
    private Adherent adherent;
    private boolean estSanctionner;
    private boolean estAbonner;
    private int quota_pret;

    public AdherentDetailDTO() {

    }

    public AdherentDetailDTO(Adherent adherent, boolean estSanctionner, boolean estAbonner, int quota_pret) {
        this.adherent = adherent;
        this.estSanctionner = estSanctionner;
        this.estAbonner = estAbonner;
        this.quota_pret = quota_pret;
    }

    public Adherent getAdherent() {
        return adherent;
    }

    public void setAdherent(Adherent adherent) {
        this.adherent = adherent;
    }

    public boolean isEstSanctionner() {
        return estSanctionner;
    }

    public void setEstSanctionner(boolean estSanctionner) {
        this.estSanctionner = estSanctionner;
    }

    public boolean isEstAbonner() {
        return estAbonner;
    }

    public void setEstAbonner(boolean estAbonner) {
        this.estAbonner = estAbonner;
    }

    public int getQuota_pret() {
        return quota_pret;
    }

    public void setQuota_pret(int quota_pret) {
        this.quota_pret = quota_pret;
    }

}
