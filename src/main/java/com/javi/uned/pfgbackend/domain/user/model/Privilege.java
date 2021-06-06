package com.javi.uned.pfgbackend.domain.user.model;

import com.javi.uned.pfgbackend.adapters.api.users.PrivilegeDTO;
import com.javi.uned.pfgbackend.adapters.database.privilege.PrivilegeEntity;
import com.javi.uned.pfgbackend.adapters.database.role.RoleEntity;

import java.util.Collection;

public class Privilege {

    private final Long id;
    private final String name;
    private final Collection<RoleEntity> roleEntities;

    public Privilege(Long id, String name, Collection<RoleEntity> roleEntities) {
        this.id = id;
        this.name = name;
        this.roleEntities = roleEntities;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Collection<RoleEntity> getRoleEntities() {
        return roleEntities;
    }

    public PrivilegeDTO toTransferObject() {
        PrivilegeDTO privilegeDTO = new PrivilegeDTO();
        privilegeDTO.setId(id);
        privilegeDTO.setName(name);
        return privilegeDTO;
    }

    public PrivilegeEntity toEntity() {
        PrivilegeEntity privilegeEntity = new PrivilegeEntity();
        privilegeEntity.setId(id);
        privilegeEntity.setName(name);
        privilegeEntity.setRoleEntities(roleEntities);
        return privilegeEntity;
    }
}
