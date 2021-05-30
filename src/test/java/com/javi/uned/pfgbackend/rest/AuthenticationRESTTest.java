package com.javi.uned.pfgbackend.rest;

import com.javi.uned.pfgbackend.beans.login.LoginDTO;
import com.javi.uned.pfgbackend.beans.login.LoginResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
class AuthenticationRESTTest {

    @Autowired
    private AuthenticationREST authenticationREST;

    @Test
    void loginCorrecto() throws Exception {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("tester@gmail.com");
        loginDTO.setPassword("5885");
        LoginResponse response = authenticationREST.login(loginDTO);
        assert response.getData().getToken().length() > 0;
    }

    @Test
    void loginErroneo() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("tester@gmail.com");
        loginDTO.setPassword("wrong-password");
        try {
            LoginResponse loginResponse = authenticationREST.login(loginDTO);
            assert false;
        } catch (Exception e) {
            assert true;
        }
    }
}