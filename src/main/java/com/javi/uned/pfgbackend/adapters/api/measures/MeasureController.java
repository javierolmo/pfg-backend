package com.javi.uned.pfgbackend.adapters.api.measures;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

public interface MeasureController {

    @GetMapping(value = "/api/measures", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity getAvailableMeasures();

}
