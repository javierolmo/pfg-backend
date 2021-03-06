package com.javi.uned.pfgbackend.adapters.api.measures;

import com.javi.uned.pfg.model.constants.Compases;
import io.swagger.annotations.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@Api(tags = "Measures")
public class MeasureControllerImpl implements MeasureController {

    public ResponseEntity getAvailableMeasures() {
        return ResponseEntity.ok(Compases.getCompases());
    }
}
