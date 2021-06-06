package com.javi.uned.pfgbackend.domain.user;

import com.javi.uned.pfgbackend.adapters.database.user.UserEntity;
import com.javi.uned.pfgbackend.adapters.database.user.UserRepository;
import com.javi.uned.pfgbackend.domain.exceptions.EntityNotFound;
import com.javi.uned.pfgbackend.domain.exceptions.ExistingUserException;
import com.javi.uned.pfgbackend.domain.exceptions.ValidationException;
import com.javi.uned.pfgbackend.domain.user.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements AuthenticationProvider {

    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public void registerUser(User user) throws ValidationException, ExistingUserException {

        // Check user is valid
        checkValid(user);

        // Create user entity
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(user.getEmail());
        userEntity.setName(user.getName());
        userEntity.setSurname(user.getSurname());
        userEntity.setEnabled(false);
        userEntity.setRoles(user.getRoles().stream().map(role -> role.toEntity()).collect(Collectors.toList()));
        userEntity.setPassword(passwordEncoder.encode(user.getPassword()));

        // Save user
        userRepository.save(userEntity);

    }

    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        // Unwrap authentication
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();

        // Check credentials
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(email);
        Optional<UserEntity> optionalUserEntity = userRepository.findOne(Example.of(userEntity));
        if (optionalUserEntity.isPresent()) {
            UserEntity existingUser = optionalUserEntity.get();
            if (passwordEncoder.matches(password, existingUser.getPassword())) {
                return new UsernamePasswordAuthenticationToken(email, password, userEntity.getRoles());
            } else {
                throw new BadCredentialsException("Bad credentials");
            }
        } else {
            throw new UsernameNotFoundException("User with email " + email + " is not registered");
        }
    }

    public User findByEmail(String email) throws EntityNotFound {

        // Fetching UserEntity optional
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(email);
        Optional<UserEntity> optionalUserEntity = userRepository.findOne(Example.of(userEntity));

        //
        if (optionalUserEntity.isPresent()) {
            userEntity = optionalUserEntity.get();
            return userEntity.toUser();
        } else {
            throw new EntityNotFound("No users with email '" + email + "'");
        }
    }

    public User findById(Long id) throws EntityNotFound {

        // Get UserEntity optional
        Optional<UserEntity> optionalUserEntity = userRepository.findById(id);
        if (optionalUserEntity.isEmpty()) {
            throw new EntityNotFound("User with id '" + id + "' not found");
        }

        // Return user
        UserEntity userEntity = optionalUserEntity.get();
        return userEntity.toUser();
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(UsernamePasswordAuthenticationToken.class);
    }

    private void checkValid(User user) throws ValidationException, ExistingUserException {

        // Check email format
        if (!user.getEmail().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new ValidationException("Invalid email");
        }

        // Check password length
        if (user.getPassword().length() < 4) {
            throw new ValidationException("Invalid password. Must contain at least 4 characters");
        }

        // Check existing email user
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(user.getEmail());
        Optional<UserEntity> optionalUserEntity = userRepository.findOne(Example.of(userEntity));
        if (optionalUserEntity.isPresent()) throw new ExistingUserException("Email already registered!");

    }

    public String generateToken(Long id, long duration) throws EntityNotFound {

        UserEntity tokenOwner = null;

        // Check if token owner exists
        Optional<UserEntity> optionalUser = userRepository.findById(id);
        if (!optionalUser.isPresent()) throw new EntityNotFound("Token owner does not exist (userid: " + id + ")");
        tokenOwner = optionalUser.get();


        // Generate personal token
        String personalToken = TokenFactory.personalToken(tokenOwner, duration);
        logger.info("Token has been generated for user {}", tokenOwner.getEmail());
        return personalToken;

    }

    public List<User> findAll() {

        // Get user entities
        List<UserEntity> userEntities = userRepository.findAll();

        //Transform to users and return
        List<User> users = userEntities.stream().map(userEntity -> userEntity.toUser()).collect(Collectors.toList());
        return users;
    }
}
