package com.javi.uned.pfgbackend.adapters.database.privilege;

import com.javi.uned.pfgbackend.domain.user.model.Privilege;

public class PrivilegeEntityTransformer {

    private PrivilegeEntityTransformer() {

    }

    public static Privilege toDomainObject(PrivilegeEntity privilegeEntity) {
        return new Privilege(
                privilegeEntity.getId(),
                privilegeEntity.getName()
        );
    }

    public static PrivilegeEntity toEntity(Privilege privilege) {
        PrivilegeEntity privilegeEntity = new PrivilegeEntity();
        privilegeEntity.setId(privilege.getId());
        privilegeEntity.setName(privilege.getName());
        return privilegeEntity;
    }

}
