package com.javi.uned.pfgbackend.adapters.api.users;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javi.uned.pfg.model.Instrumento;
import com.javi.uned.pfg.model.Specs;
import com.javi.uned.pfgbackend.adapters.filesystem.FileService;
import com.javi.uned.pfgbackend.domain.sheet.SheetService;
import com.javi.uned.pfgbackend.domain.sheet.model.Sheet;
import com.javi.uned.pfgbackend.config.JWTAuthorizationFilter;
import com.javi.uned.pfgbackend.domain.exceptions.AuthException;
import com.javi.uned.pfgbackend.domain.exceptions.EntityNotFound;
import com.javi.uned.pfgbackend.domain.instrument.InstrumentService;
import com.javi.uned.pfgbackend.domain.user.UserService;
import com.javi.uned.pfgbackend.domain.user.model.User;
import com.javi.uned.pfgbackend.domain.user.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@Api(tags = "Users")
public class UserControllerImpl implements UserController {

    private final Logger logger = LoggerFactory.getLogger(UserControllerImpl.class);
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private SheetService sheetService;
    @Autowired
    private KafkaTemplate<String, Specs> specsTemplate;
    @Autowired
    private InstrumentService instrumentService;
    @Autowired
    private FileService fileService;
    @Autowired
    private UserService userService;

    @Override
    public ResponseEntity getUsers() {

        // Get users
        List<User> users = userService.findAll();

        // Transform to DTOs and return
        List<UserDTO> userDTOs = users.stream().map(user -> user.toTransferObject()).collect(Collectors.toList());
        return ResponseEntity.ok(userDTOs);
    }

    public ResponseEntity getDetails(long id) {

        try {

            // Get user
            User user = userService.findById(id);
            UserDTO userDTO = user.toTransferObject();
            return ResponseEntity.ok(userDTO);

        } catch (EntityNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    public ResponseEntity composeSheet(Specs specs, Long userId) throws IOException {

        //Create new sheet
        Sheet sheet = new Sheet(
                null,
                specs.getMovementTitle(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")),
                userId,
                false
        );

        //Save in database
        sheet = sheetService.save(sheet);

        //Complete instruments info
        Instrumento[] instrumentosIncompletos = specs.getInstrumentos();
        List<Instrumento> instrumentosCompletos = new ArrayList<>();
        for (Instrumento instrumentoIncompleto : instrumentosIncompletos) {
            instrumentosCompletos.add(instrumentService.completarInstrumento(instrumentoIncompleto));
        }
        specs.setInstrumentos(instrumentosCompletos.toArray(new Instrumento[]{}));

        // Save request in json
        ObjectMapper objectMapper = new ObjectMapper();
        File specsFile = new File(fileService.getSheetFolder(sheet.getId()), "specs.json");
        objectMapper.writeValue(specsFile, specs);

        //Order composition request
        String sheetid = "" + sheet.getId();
        specsTemplate.send("melodia.backend.specs", sheetid, specs);

        return ResponseEntity.ok(sheet);
    }

    public ResponseEntity generateToken(Long id, long duration, HttpServletRequest request) {

        try {
            // Check if requestor exists
            JWTAuthorizationFilter jwtAuthorizationFilter = new JWTAuthorizationFilter();
            Claims claims = jwtAuthorizationFilter.validateToken(request);
            Long requestorId = ((Integer) claims.get("id")).longValue();
            User user = userService.findById(requestorId);

            // Check if user is requesting his own token
            if (!requestorId.equals(id)) throw new AuthException("You cannot request a token from another user");

            // Generate token and return
            String token = userService.generateToken(id, duration);
            return ResponseEntity.ok(token);

        } catch (EntityNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (AuthException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }

    }


}
