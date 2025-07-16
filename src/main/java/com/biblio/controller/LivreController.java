package com.biblio.controller;

import com.biblio.model.utils.LivreDetailDT0;
import com.biblio.service.LivreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/livre")
public class LivreController {

    @Autowired
    private LivreService livreService;

    @GetMapping("/detail/{id}")
    public LivreDetailDT0 getDetailById(@PathVariable int id) throws Exception {
        return livreService.getDetailByIdLivre(id);
    }
}
