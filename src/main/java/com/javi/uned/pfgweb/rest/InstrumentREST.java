package com.javi.uned.pfgweb.rest;

import com.javi.uned.pfg.model.Instrumento;
import com.javi.uned.pfg.model.constants.Instrumentos;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/api/instruments")
@Api(tags = "Instruments")
public class InstrumentREST {

    @GetMapping
    public Instrumento[] getAvailableInstruments() {
        return Instrumentos.getInstrumentos();
    }
}
