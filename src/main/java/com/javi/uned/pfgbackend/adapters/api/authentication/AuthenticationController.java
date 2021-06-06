package com.javi.uned.pfgbackend.adapters.api.authentication;

import com.javi.uned.pfgbackend.adapters.api.authentication.model.LoginDTO;
import com.javi.uned.pfgbackend.adapters.api.authentication.model.LoginResponse;
import com.javi.uned.pfgbackend.adapters.api.users.UserDTO;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthenticationController {

    @PostMapping(value= "/api/auth/login", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity login(@RequestBody LoginDTO user);

    @PostMapping(value= "/api/auth/register", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity register(@RequestBody UserDTO userDTO);
}