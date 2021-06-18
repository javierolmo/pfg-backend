package com.javi.uned.pfgbackend.adapters.api.instruments;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

public interface InstrumentController {

    @GetMapping(value = "/api/instruments", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity getAvailableInstruments();

}
