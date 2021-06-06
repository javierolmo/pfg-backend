package com.javi.uned.pfgbackend.adapters.api.users;

import com.javi.uned.pfgbackend.adapters.database.role.RoleEntity;

import java.util.Collection;

public class PrivilegeDTO {

    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
