package com.javi.uned.pfgbackend.adapters.api.measures;

import com.javi.uned.pfg.model.Compas;
import com.javi.uned.pfg.model.constants.Compases;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

public interface MeasureController {

    @GetMapping(value = "/api/measures", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity getAvailableMeasures();

}
