package com.javi.uned.pfgbackend.domain.user.model;

import com.javi.uned.pfgbackend.adapters.api.users.RoleDTO;
import com.javi.uned.pfgbackend.adapters.database.privilege.PrivilegeEntity;
import com.javi.uned.pfgbackend.adapters.database.role.RoleEntity;
import com.javi.uned.pfgbackend.adapters.database.user.UserEntity;

import java.util.Collection;
import java.util.stream.Collectors;

public class Role {

    private final Long id;
    private final String name;
    private final Collection<UserEntity> userEntities;
    private final Collection<Privilege> privileges;

    public Role(Long id, String name, Collection<UserEntity> userEntities, Collection<Privilege> privileges) {
        this.id = id;
        this.name = name;
        this.userEntities = userEntities;
        this.privileges = privileges;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Collection<UserEntity> getUserEntities() {
        return userEntities;
    }

    public Collection<Privilege> getPrivileges() {
        return privileges;
    }

    public RoleDTO toTransferObject() {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(id);
        roleDTO.setName(name);
        roleDTO.setPrivileges(privileges.stream().map(privilege -> privilege.toTransferObject()).collect(Collectors.toList()));
        return roleDTO;
    }

    public RoleEntity toEntity() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(id);
        roleEntity.setName(name);
        roleEntity.setUserEntities(userEntities);
        roleEntity.setPrivilegeEntities(privileges.stream().map(privilege -> privilege.toEntity()).collect(Collectors.toList()));
        return roleEntity;
    }
}
