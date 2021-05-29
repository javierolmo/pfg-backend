package com.javi.uned.pfgweb.util;

import com.javi.uned.pfgweb.beans.Role;
import com.javi.uned.pfgweb.beans.User;
import com.javi.uned.pfgweb.config.JWTAuthorizationFilter;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class TokenFactory {

    private TokenFactory () {

    }

    public static String authToken(Authentication authentication, User user){

        List<GrantedAuthority> grantedAuthorities = (List<GrantedAuthority>) authentication.getAuthorities();
        String token = Jwts.builder()
                .setId("auth-token")
                .setSubject(authentication.getName())
                .claim("id", user.getId())
                .claim("name", user.getName())
                .claim("surname", user.getSurname())
                .claim("email", user.getEmail())
                .claim("authorities", grantedAuthorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 600000))
                .signWith(SignatureAlgorithm.HS512, JWTAuthorizationFilter.SECRET.getBytes())
                .compact();
        return JWTAuthorizationFilter.PREFIX + token;
    }

    public static String personalToken(User user, long duration) {

        String token = Jwts.builder()
                .setId("personal-token")
                .claim("id", user.getId())
                .claim("authorities", user.getRoles().stream().map(Role::getAuthority).collect(Collectors.toList()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + duration))
                .signWith(SignatureAlgorithm.HS512, JWTAuthorizationFilter.SECRET.getBytes())
                .compact();
        return JWTAuthorizationFilter.PREFIX + token;

    }
}
