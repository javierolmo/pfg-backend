package com.javi.uned.pfgweb.rest;

import com.javi.uned.pfg.model.Tonalidad;
import com.javi.uned.pfg.model.constants.Tonalidades;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/tonalities")
@Api(tags = "Tonality")
public class TonalityREST {

    @GetMapping
    public Tonalidad[] getAvailableTonality() {
        return Tonalidades.getTonalidades();
    }

}
