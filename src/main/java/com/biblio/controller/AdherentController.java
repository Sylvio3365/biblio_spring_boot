package com.biblio.controller;

import com.biblio.model.Adherent;
import com.biblio.model.utils.AdherentDetailDTO;
import com.biblio.model.utils.LivreDetailDT0;
import com.biblio.service.AdherentService;
import com.biblio.service.LivreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/adherent")
public class AdherentController {

    @Autowired
    private AdherentService adherentService;

    @GetMapping("/detail/{id}")
    public AdherentDetailDTO getDetailById(@PathVariable int id) throws Exception {
        return adherentService.getDetailAdherent(id);
    }
}
