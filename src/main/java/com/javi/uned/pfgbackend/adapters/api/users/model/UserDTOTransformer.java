package com.javi.uned.pfgbackend.adapters.api.users.model;

import com.javi.uned.pfgbackend.adapters.api.authentication.model.RegistrationRequest;
import com.javi.uned.pfgbackend.adapters.api.roles.RoleDTO;
import com.javi.uned.pfgbackend.adapters.api.roles.RoleDTOTransformer;
import com.javi.uned.pfgbackend.domain.user.model.Role;
import com.javi.uned.pfgbackend.domain.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserDTOTransformer {

    private UserDTOTransformer() {

    }

    public static User toDomainObject(UserDTO userDTO) {

        List<Role> roles = userDTO.getRoles().stream()
                .map(roleDTO -> RoleDTOTransformer.toDomainObject(roleDTO))
                .collect(Collectors.toList());

        return new User(
                userDTO.getId(),
                userDTO.getEmail(),
                userDTO.getPassword(),
                userDTO.getName(),
                userDTO.getSurname(),
                userDTO.getEnabled(),
                roles
        );
    }

    public static User toDomainObject(RegistrationRequest registrationRequest) {

        return new User(
                null,
                registrationRequest.getEmail(),
                registrationRequest.getPassword(),
                registrationRequest.getFirstName(),
                registrationRequest.getLastName(),
                true,
                new ArrayList<>()
        );
    }

    public static UserDTO toTransferObject(User user) {

        List<RoleDTO> roleDTOs = user.getRoles().stream()
                .map(RoleDTOTransformer::toTransferObject)
                .collect(Collectors.toList());

        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword());
        userDTO.setName(user.getName());
        userDTO.setSurname(user.getSurname());
        userDTO.setEnabled(user.getEnabled());
        userDTO.setRoles(roleDTOs);

        return userDTO;
    }
}
