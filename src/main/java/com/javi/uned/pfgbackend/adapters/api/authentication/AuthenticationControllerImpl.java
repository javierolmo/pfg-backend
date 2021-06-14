package com.javi.uned.pfgbackend.adapters.api.authentication;

import com.javi.uned.pfgbackend.adapters.api.authentication.model.LoginDTO;
import com.javi.uned.pfgbackend.adapters.api.authentication.model.LoginResponse;
import com.javi.uned.pfgbackend.adapters.api.authentication.model.LoginResponseData;
import com.javi.uned.pfgbackend.adapters.api.users.UserDTOTransformer;
import com.javi.uned.pfgbackend.domain.exceptions.ExistingUserException;
import com.javi.uned.pfgbackend.domain.exceptions.ValidationException;
import com.javi.uned.pfgbackend.domain.user.TokenFactory;
import com.javi.uned.pfgbackend.adapters.api.users.UserDTO;
import com.javi.uned.pfgbackend.adapters.database.user.UserEntity;
import com.javi.uned.pfgbackend.config.WebSecurityConfig;
import com.javi.uned.pfgbackend.domain.user.UserService;
import com.javi.uned.pfgbackend.domain.user.model.User;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@Api(tags = "Authentication")
public class AuthenticationControllerImpl implements AuthenticationController {

    private final Logger logger = LoggerFactory.getLogger(AuthenticationControllerImpl.class);

    @Autowired
    private WebSecurityConfig webSecurityConfig;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;

    public ResponseEntity login(LoginDTO user) {
        try {
            AuthenticationManager authenticationManager = webSecurityConfig.authenticationManagerBean();
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
            logger.info("Login successful: {}", user.getEmail());

            LoginResponse loginResponse = new LoginResponse();
            LoginResponseData data = new LoginResponseData();
            data.setToken(TokenFactory.authToken(authentication, userService.findByEmail(user.getEmail())));
            loginResponse.setData(data);

            return ResponseEntity.ok(loginResponse);
        } catch (Exception e) {
            logger.error("Login failed: {}", user.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    public ResponseEntity register(UserDTO userDTO) {
        try {
            User user = UserDTOTransformer.toDomainObject(userDTO);
            userService.registerUser(user);
            return ResponseEntity.ok(userDTO);
        } catch (ValidationException e) {
            logger.warn("Invalid sign up request (user: {})", userDTO.getEmail());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (ExistingUserException e) {
            logger.warn("Already existing user trying to sign up (user: {})", userDTO.getEmail());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

}
