package com.javi.uned.pfgbackend.domain.user;

import com.javi.uned.pfgbackend.adapters.database.role.RoleEntity;
import com.javi.uned.pfgbackend.adapters.database.user.UserEntity;
import com.javi.uned.pfgbackend.config.JWTAuthorizationFilter;
import com.javi.uned.pfgbackend.domain.user.model.User;
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

    public static String personalToken(UserEntity userEntity, long duration) {

        String token = Jwts.builder()
                .setId("personal-token")
                .claim("id", userEntity.getId())
                .claim("authorities", userEntity.getRoles().stream().map(RoleEntity::getAuthority).collect(Collectors.toList()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + duration))
                .signWith(SignatureAlgorithm.HS512, JWTAuthorizationFilter.SECRET.getBytes())
                .compact();
        return JWTAuthorizationFilter.PREFIX + token;

    }
}
