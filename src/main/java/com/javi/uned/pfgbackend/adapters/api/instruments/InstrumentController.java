package com.javi.uned.pfgbackend.adapters.api.instruments;

import com.javi.uned.pfg.model.Instrumento;
import com.javi.uned.pfg.model.constants.Instrumentos;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

public interface InstrumentController {

    @GetMapping(value = "/api/instruments", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity getAvailableInstruments();

}
