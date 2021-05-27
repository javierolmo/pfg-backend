package com.javi.uned.pfgweb.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CustomUserDetailsServiceTest {


    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void GetUserDetails_ByUsername_Success() {
        assertNotNull(customUserDetailsService.loadUserByUsername("tester@gmail.com"));
    }

    @Test
    void GetUserDetails_ByUsername_MissingUsername() {
        assertThrows(UsernameNotFoundException.class, () -> customUserDetailsService.loadUserByUsername("invalid_username_test"));
    }
}