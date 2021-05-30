package com.javi.uned.pfgbackend.services;

import com.javi.uned.pfgbackend.beans.User;
import com.javi.uned.pfgbackend.config.BeansConfig;
import com.javi.uned.pfgbackend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service("userDetailsService")
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BeansConfig beansConfig;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public User findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public User createUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public boolean existsByEmail(String email){
        User user = new User();
        user.setEmail(email);
        Example<User> example = Example.of(user);
        return userRepository.exists(example);
    }

    public void deleteById(long id){
        userRepository.deleteById(id);
    }

}
