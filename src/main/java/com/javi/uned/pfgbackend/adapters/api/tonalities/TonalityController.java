package com.javi.uned.pfgbackend.adapters.api.tonalities;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

public interface TonalityController {

    /**
     * Obtiene el conjunto de tonalidades disponibles
     *
     * @return
     */
    @GetMapping(value = "/api/tonalities", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity getAvailableTonality();
}
