package com.biblio.model.utils;

import java.util.List;

import com.biblio.model.Livre;

public class LivreDetailDT0 {
    private Livre livre;
    private List<ExemplaireDetailDTO> exemplaireDetail;

    public LivreDetailDT0(Livre livre, List<ExemplaireDetailDTO> exemplaireDetail) {
        this.livre = livre;
        this.exemplaireDetail = exemplaireDetail;
    }

    public LivreDetailDT0() {
    }

    public Livre getLivre() {
        return livre;
    }

    public void setLivre(Livre livre) {
        this.livre = livre;
    }

    public List<ExemplaireDetailDTO> getExemplaireDetail() {
        return exemplaireDetail;
    }

    public void setExemplaireDetail(List<ExemplaireDetailDTO> exemplaireDetail) {
        this.exemplaireDetail = exemplaireDetail;
    }
}
