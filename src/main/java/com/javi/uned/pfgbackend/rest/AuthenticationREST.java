package com.javi.uned.pfgbackend.rest;

import com.javi.uned.pfgbackend.beans.login.LoginDTO;
import com.javi.uned.pfgbackend.beans.login.LoginResponse;
import com.javi.uned.pfgbackend.beans.login.LoginResponseData;
import com.javi.uned.pfgbackend.config.WebSecurityConfig;
import com.javi.uned.pfgbackend.services.UserService;
import com.javi.uned.pfgbackend.util.TokenFactory;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/auth")
@Api(tags = "Authentication")
public class AuthenticationREST {

    private final Logger logger = LoggerFactory.getLogger(AuthenticationREST.class);

    @Autowired
    private WebSecurityConfig webSecurityConfig;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;

    @PostMapping(value= "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public LoginResponse login(@RequestBody LoginDTO user) throws Exception {
        AuthenticationManager authenticationManager = webSecurityConfig.authenticationManagerBean();
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));

        LoginResponse loginResponse = new LoginResponse();
        LoginResponseData data = new LoginResponseData();
        data.setToken(TokenFactory.authToken(authentication, userService.findByEmail(user.getEmail())));
        loginResponse.setData(data);

        return loginResponse;
    }

}
