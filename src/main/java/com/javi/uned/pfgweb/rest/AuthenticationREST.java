package com.javi.uned.pfgweb.rest;

import com.javi.uned.pfgweb.beans.login.LoginDTO;
import com.javi.uned.pfgweb.beans.login.LoginResponse;
import com.javi.uned.pfgweb.beans.login.LoginResponseData;
import com.javi.uned.pfgweb.config.WebSecurityConfig;
import com.javi.uned.pfgweb.services.UserService;
import com.javi.uned.pfgweb.util.TokenFactory;
import io.swagger.annotations.Api;
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
