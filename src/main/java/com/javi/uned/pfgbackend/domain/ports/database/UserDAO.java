package com.javi.uned.pfgbackend.domain.ports.database;

import com.javi.uned.pfgbackend.domain.exceptions.EntityNotFound;
import com.javi.uned.pfgbackend.domain.user.model.User;

import java.util.List;

public interface UserDAO {

    public User save(User user);

    User findById(Long id) throws EntityNotFound;

    boolean existsById(Long id);

    List<User> findAll();

    boolean existsByEmail(String email);

    User findByEmail(String email) throws EntityNotFound;
}
