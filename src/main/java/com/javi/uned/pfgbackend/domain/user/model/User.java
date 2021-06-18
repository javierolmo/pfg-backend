package com.javi.uned.pfgbackend.domain.user.model;

import java.util.Collection;

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

}
