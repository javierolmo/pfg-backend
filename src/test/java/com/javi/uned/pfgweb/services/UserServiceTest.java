package com.javi.uned.pfgweb.services;

import com.javi.uned.pfgweb.beans.User;
import com.javi.uned.pfgweb.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private UserRepository userRepository;

    @Test
    void findByEmail() {
        User user = userService.findByEmail("tester@gmail.com");
        assertNotNull(user);
    }

    @Test
    void createDeleteUser() {
        User user = new User();
        user.setEmail("testemail@testdomain.com");
        user.setPassword("1234");
        User createdUser = userService.createUser(user);
        assertNotNull(createdUser);
        assertTrue(encoder.matches("1234", createdUser.getPassword()));
        userService.deleteById(createdUser.getId());
        assertFalse(userRepository.findById(createdUser.getId()).isPresent());
    }

    @Test
    void exists() {
        assert userService.existsByEmail("tester@gmail.com");
    }
}