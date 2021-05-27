package com.javi.uned.pfgweb.rest;

import com.javi.uned.pfgweb.beans.login.LoginDTO;
import com.javi.uned.pfgweb.beans.login.LoginResponse;
import com.javi.uned.pfgweb.beans.User;
import com.javi.uned.pfgweb.beans.login.LoginResponseData;
import com.javi.uned.pfgweb.config.WebSecurityConfig;
import com.javi.uned.pfgweb.services.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
        data.setToken(getJWTToken(authentication, userService.findByEmail(user.getEmail())));
        loginResponse.setData(data);
        return loginResponse;
    }

    private String getJWTToken(Authentication authentication, User user){

        String secretKey = "1234";
        List<GrantedAuthority> grantedAuthorities = (List<GrantedAuthority>) authentication.getAuthorities();
        String token = Jwts.builder()
                .setId("softtekJWT")
                .setSubject(authentication.getName())
                .claim("id", user.getId())
                .claim("name", user.getName())
                .claim("surname", user.getSurname())
                .claim("email", user.getEmail())
                .claim("authorities", grantedAuthorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 600000))
                .signWith(SignatureAlgorithm.HS512, secretKey.getBytes())
                .compact();
        return "Bearer " + token;
    }

}
