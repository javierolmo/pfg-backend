package com.javi.uned.pfgbackend.adapters.api.tonalities;

import com.javi.uned.pfg.model.constants.Tonalidades;
import io.swagger.annotations.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@Api(tags = "Tonality")
public class TonalityControllerImpl implements TonalityController {

    public ResponseEntity getAvailableTonality() {
        return ResponseEntity.ok(Tonalidades.getTonalidades());
    }

}
