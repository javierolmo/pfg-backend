package com.javi.uned.pfgbackend.adapters.database.sheet;

import com.javi.uned.pfgbackend.adapters.database.privilege.PrivilegeEntity;
import com.javi.uned.pfgbackend.adapters.database.privilege.PrivilegeEntityTransformer;
import com.javi.uned.pfgbackend.adapters.database.role.RoleEntity;
import com.javi.uned.pfgbackend.domain.sheet.model.Sheet;
import com.javi.uned.pfgbackend.domain.user.model.Privilege;
import com.javi.uned.pfgbackend.domain.user.model.Role;

import java.util.List;
import java.util.stream.Collectors;

public class SheetEntityTransformer {

    private SheetEntityTransformer() {

    }

    public static Sheet toDomainObject(SheetEntity sheetEntity) {

        return new Sheet(
                sheetEntity.getId(),
                sheetEntity.getName(),
                sheetEntity.getDate(),
                sheetEntity.getOwnerId(),
                sheetEntity.getFinished()
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
