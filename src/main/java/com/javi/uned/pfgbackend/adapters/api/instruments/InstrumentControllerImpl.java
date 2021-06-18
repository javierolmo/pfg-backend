package com.javi.uned.pfgbackend.adapters.api.instruments;

import com.javi.uned.pfg.model.constants.Instrumentos;
import io.swagger.annotations.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@Api(tags = "Instruments")
public class InstrumentControllerImpl implements InstrumentController {

    public ResponseEntity getAvailableInstruments() {
        return ResponseEntity.ok(Instrumentos.getInstrumentos());
    }
}
