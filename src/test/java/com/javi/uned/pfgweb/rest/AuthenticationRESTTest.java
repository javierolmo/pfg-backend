package com.javi.uned.pfgweb.rest;

import com.javi.uned.pfgweb.beans.login.LoginDTO;
import com.javi.uned.pfgweb.beans.login.LoginResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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