package com.javi.uned.pfgbackend.domain.user.model;

import com.javi.uned.pfgbackend.adapters.api.users.UserDTO;
import com.javi.uned.pfgbackend.adapters.database.role.RoleEntity;
import com.javi.uned.pfgbackend.adapters.database.user.UserEntity;

import java.util.Collection;
import java.util.stream.Collectors;

public class User {

    private final Long id;
    private final String email;
    private final String password;
    private final String name;
    private final String surname;
    private final Boolean enabled;
    private final Collection<Role> roles;

    public User(Long id, String email, String password, String name, String surname, Boolean enabled, Collection<Role> roles) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.enabled = enabled;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public UserDTO toTransferObject() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(id);
        userDTO.setEmail(email);
        userDTO.setName(name);
        userDTO.setSurname(surname);
        userDTO.setEnabled(enabled);
        userDTO.setRoles(roles.stream().map(role -> role.toTransferObject()).collect(Collectors.toList()));
        return userDTO;
    }

    public UserEntity toEntity() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(id);
        userEntity.setEmail(email);
        userEntity.setPassword(password);
        userEntity.setName(name);
        userEntity.setSurname(surname);
        userEntity.setEnabled(enabled);
        userEntity.setRoles(roles.stream().map(role -> role.toEntity()).collect(Collectors.toList()));
        return userEntity;
    }

}
