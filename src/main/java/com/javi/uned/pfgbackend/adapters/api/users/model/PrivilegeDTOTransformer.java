package com.javi.uned.pfgbackend.adapters.api.users.model;

import com.javi.uned.pfgbackend.domain.user.model.Privilege;

public class PrivilegeDTOTransformer {

    private PrivilegeDTOTransformer () {

    }

    public static Privilege toDomainObject(PrivilegeDTO privilegeDTO) {
        return new Privilege(
                privilegeDTO.getId(),
                privilegeDTO.getName()
        );
    }

    public static PrivilegeDTO toTransferObject(Privilege privilege) {
        PrivilegeDTO privilegeDTO = new PrivilegeDTO();
        privilegeDTO.setId(privilege.getId());
        privilegeDTO.setName(privilege.getName());
        return privilegeDTO;
    }
}
