package com.javi.uned.pfgbackend.adapters.api.users.model;

import java.util.ArrayList;
import java.util.Collection;

public class RoleDTO {

    private long id;
    private String name;
    private Collection<PrivilegeDTO> privileges;

    public RoleDTO() {
        privileges = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<PrivilegeDTO> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(Collection<PrivilegeDTO> privileges) {
        this.privileges = privileges;
    }
}
