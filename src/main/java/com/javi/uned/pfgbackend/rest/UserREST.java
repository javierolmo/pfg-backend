package com.javi.uned.pfgbackend.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javi.uned.pfg.model.Instrumento;
import com.javi.uned.pfg.model.Specs;
import com.javi.uned.pfgbackend.beans.Sheet;
import com.javi.uned.pfgbackend.beans.User;
import com.javi.uned.pfgbackend.config.FileSystemConfig;
import com.javi.uned.pfgbackend.config.JWTAuthorizationFilter;
import com.javi.uned.pfgbackend.exceptions.AuthException;
import com.javi.uned.pfgbackend.exceptions.RepositoryException;
import com.javi.uned.pfgbackend.repositories.SheetRepository;
import com.javi.uned.pfgbackend.repositories.UserRepository;
import com.javi.uned.pfgbackend.services.CustomUserDetailsService;
import com.javi.uned.pfgbackend.services.UtilService;
import com.javi.uned.pfgbackend.util.TokenFactory;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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

    private final Logger logger = LoggerFactory.getLogger(UserREST.class);

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

    /**
     * Generate a token for specified user. Only for own token
     * @param id identifier of user
     * @param duration token duration in millis (default 30 days)
     * @param request complete request to retrieve headers
     * @return valid token for user
     */
    @GetMapping("/{id}/token")
    public ResponseEntity<String> generateToken(
            @PathVariable Long id,
            @RequestParam(defaultValue = "2592000000", required = false) long duration,
            HttpServletRequest request) {

        User tokenOwner = null;
        User requestor = null;

        try {

            // Check if token owner exists
            Optional<User> optionalUser = userRepository.findById(id);
            if(!optionalUser.isPresent()) throw new RepositoryException("Token owner does not exist (userid: " + id + ")");
            tokenOwner = optionalUser.get();

            // Check if requestor exists
            JWTAuthorizationFilter jwtAuthorizationFilter = new JWTAuthorizationFilter();
            Claims claims = jwtAuthorizationFilter.validateToken(request);
            Optional<User> optionalRequestorUser = userRepository.findById(((Integer) claims.get("id")).longValue());
            if(!optionalRequestorUser.isPresent()) throw new RepositoryException("Requestor user does not exist (userid: " + id + ")");
            requestor = optionalRequestorUser.get();

            // Check if user is requesting his own token
            if(!requestor.getId().equals(id)) throw new AuthException("You cannot request a token from another user");

            // Generate personal token
            String personalToken = TokenFactory.personalToken(tokenOwner, duration);

            logger.info("Token has been generated for user {}", tokenOwner.getEmail());
            return ResponseEntity.ok(personalToken);

        } catch (AuthException ae) {
            String requestorEmail = requestor == null? "unknown" : requestor.getEmail();
            logger.error("Token can only be requested by owner. Requestor: {}, Token owner: {}", requestorEmail, tokenOwner.getEmail());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ae.getMessage());
        } catch (RepositoryException re) {
            logger.error(re.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(re.getMessage());
        }

    }


}
