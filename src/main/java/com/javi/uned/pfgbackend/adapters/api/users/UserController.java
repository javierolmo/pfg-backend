package com.javi.uned.pfgbackend.adapters.api.users;

import com.javi.uned.pfg.model.Specs;
import com.javi.uned.pfgbackend.adapters.database.sheet.Sheet;
import com.javi.uned.pfgbackend.adapters.database.user.UserEntity;
import com.javi.uned.pfgbackend.domain.exceptions.EntityNotFound;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public interface UserController {

    /**
     * TODO:
     * @return
     */
    @GetMapping(value = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity getUsers();

    /**
     * TODO:
     * @param id
     * @return
     */
    @GetMapping(value = "/api/users/{id}/details", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity getDetails(@PathVariable long id) throws EntityNotFound;

    /**
     * TODO:
     * @param specs
     * @param userId
     * @return
     * @throws IOException
     */
    @PostMapping(value = "/api/users/{userId}/request", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity composeSheet(@RequestBody Specs specs, @PathVariable Long userId) throws IOException;

    /**
     * Generate a token for specified user. Only for own token
     *
     * @param id       identifier of user
     * @param duration token duration in millis (default 30 days)
     * @param request  complete request to retrieve headers
     * @return valid token for user
     */
    @GetMapping(value = "/api/users/{id}/token", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity generateToken(
            @PathVariable Long id,
            @RequestParam(defaultValue = "2592000000", required = false) long duration,
            HttpServletRequest request);
}
