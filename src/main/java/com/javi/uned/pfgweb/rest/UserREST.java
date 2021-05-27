package com.javi.uned.pfgweb.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javi.uned.pfg.model.Instrumento;
import com.javi.uned.pfg.model.Specs;
import com.javi.uned.pfgweb.beans.Sheet;
import com.javi.uned.pfgweb.beans.User;
import com.javi.uned.pfgweb.config.FileSystemConfig;
import com.javi.uned.pfgweb.repositories.SheetRepository;
import com.javi.uned.pfgweb.repositories.UserRepository;
import com.javi.uned.pfgweb.services.CustomUserDetailsService;
import com.javi.uned.pfgweb.services.UtilService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/users")
@Api(tags = "Users")
public class UserREST {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SheetRepository sheetRepository;
    @Autowired
    private KafkaTemplate<String, Specs> specsTemplate;
    @Autowired
    private UtilService utilService;
    @Autowired
    private FileSystemConfig fileSystemConfig;

    @GetMapping("/{id}/details")
    public User getDetails(@PathVariable long id){
        Optional<User> optionalUser = userRepository.findById(id);
        return optionalUser.isPresent()? optionalUser.get() : null;
    }

    @PostMapping("/{userId}/request")
    public Sheet composeSheet(@RequestBody Specs specs, @PathVariable Long userId) throws IOException {

        //Create new sheet
        Sheet sheet = new Sheet();
        sheet.setDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
        sheet.setOwnerId(userId);
        sheet.setName(specs.getMovementTitle());
        sheet.setFinished(false);

        //Save in database
        sheetRepository.save(sheet);

        //Complete instruments info
        Instrumento[] instrumentosIncompletos = specs.getInstrumentos();
        List<Instrumento> instrumentosCompletos = new ArrayList<>();
        for(Instrumento instrumentoIncompleto: instrumentosIncompletos){
            instrumentosCompletos.add(utilService.completarInstrumento(instrumentoIncompleto));
        }
        specs.setInstrumentos(instrumentosCompletos.toArray(new Instrumento[]{}));

        // Save request in json
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(new File(fileSystemConfig.getSheetFolder(sheet.getId()), "specs.json"), specs);

        //Order composition request
        String sheetid = ""+sheet.getId();
        specsTemplate.send("melodia.backend.specs", sheetid, specs);

        return sheet;
    }


}
