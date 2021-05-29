package com.javi.uned.pfgweb.rest;

import com.javi.uned.pfg.model.Compas;
import com.javi.uned.pfg.model.constants.Compases;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/api/measures")
@Api(tags = "Measures")
public class MeasureREST {

    @GetMapping
    public Compas[] getAvailableMeasures() {
        return Compases.getCompases();
    }
}
