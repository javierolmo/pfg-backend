package com.javi.uned.pfgbackend.adapters.api.roles;

import com.javi.uned.pfgbackend.adapters.api.privileges.PrivilegeDTO;
import com.javi.uned.pfgbackend.adapters.api.privileges.PrivilegeDTOTransformer;
import com.javi.uned.pfgbackend.domain.user.model.Privilege;
import com.javi.uned.pfgbackend.domain.user.model.Role;

import java.util.List;
import java.util.stream.Collectors;

public class RoleDTOTransformer {

    private RoleDTOTransformer() {

    }

    public static Role toDomainObject(RoleDTO roleDTO) {

        List<Privilege> privileges = roleDTO.getPrivileges().stream()
                .map(PrivilegeDTOTransformer::toDomainObject)
                .collect(Collectors.toList());

        return new Role(
                roleDTO.getId(),
                roleDTO.getName(),
                privileges
        );
    }

    public static RoleDTO toTransferObject(Role role) {

        List<PrivilegeDTO> privilegeDTOs = role.getPrivileges().stream()
                .map(PrivilegeDTOTransformer::toTransferObject)
                .collect(Collectors.toList());

        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(role.getId());
        roleDTO.setName(role.getName());
        roleDTO.setPrivileges(privilegeDTOs);
        return roleDTO;
    }
}
