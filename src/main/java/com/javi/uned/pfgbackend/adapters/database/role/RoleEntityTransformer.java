package com.javi.uned.pfgbackend.adapters.database.role;

import com.javi.uned.pfgbackend.adapters.database.privilege.PrivilegeEntity;
import com.javi.uned.pfgbackend.adapters.database.privilege.PrivilegeEntityTransformer;
import com.javi.uned.pfgbackend.domain.user.model.Privilege;
import com.javi.uned.pfgbackend.domain.user.model.Role;

import java.util.List;
import java.util.stream.Collectors;

public class RoleEntityTransformer {

    private RoleEntityTransformer() {

    }

    public static Role toDomainObject(RoleEntity roleEntity) {

        List<Privilege> privileges = roleEntity.getPrivilegeEntities().stream()
                .map(PrivilegeEntityTransformer::toDomainObject)
                .collect(Collectors.toList());

        return new Role(
                roleEntity.getId(),
                roleEntity.getName(),
                privileges
        );
    }

    public static RoleEntity toEntity(Role role) {

        List<PrivilegeEntity> privilegeEntities = role.getPrivileges().stream()
                .map(PrivilegeEntityTransformer::toEntity)
                .collect(Collectors.toList());

        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(role.getId());
        roleEntity.setName(role.getName());
        roleEntity.setPrivilegeEntities(privilegeEntities);
        return roleEntity;
    }

}
