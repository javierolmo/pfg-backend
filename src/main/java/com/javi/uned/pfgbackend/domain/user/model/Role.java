package com.javi.uned.pfgbackend.domain.user.model;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class Role implements GrantedAuthority {

    private final Long id;
    private final String name;
    private final Collection<Privilege> privileges;

    public Role(Long id, String name, Collection<Privilege> privileges) {
        this.id = id;
        this.name = name;
        this.privileges = privileges;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Collection<Privilege> getPrivileges() {
        return privileges;
    }

    @Override
    public String getAuthority() {
        return this.name;
    }
}
