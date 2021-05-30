package com.javi.uned.pfgbackend.services;

import com.javi.uned.pfg.model.Instrumento;
import com.javi.uned.pfg.model.constants.Instrumentos;
import org.springframework.stereotype.Service;

@Service
public class UtilService {

    public Instrumento completarInstrumento(Instrumento instrumentoIncompleto){
        for(Instrumento instrumento : Instrumentos.getInstrumentos()){
            if(instrumento.getRef().equals(instrumentoIncompleto.getRef())) return instrumento;
        }
        throw new IllegalArgumentException("No se ha podido encontrar el instrumento "+instrumentoIncompleto.getRef());
    }
}
