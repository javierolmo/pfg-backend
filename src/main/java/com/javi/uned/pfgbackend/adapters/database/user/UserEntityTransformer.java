package com.javi.uned.pfgbackend.adapters.database.user;

import com.javi.uned.pfgbackend.adapters.database.role.RoleEntity;
import com.javi.uned.pfgbackend.adapters.database.role.RoleEntityTransformer;
import com.javi.uned.pfgbackend.domain.user.model.Role;
import com.javi.uned.pfgbackend.domain.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

public class UserEntityTransformer {

    private UserEntityTransformer() {

    }

    public static User toDomainObject(UserEntity userEntity) {

        List<Role> roles = userEntity.getRoles().stream()
                .map(RoleEntityTransformer::toDomainObject)
                .collect(Collectors.toList());

        return new User(
                userEntity.getId(),
                userEntity.getEmail(),
                userEntity.getPassword(),
                userEntity.getName(),
                userEntity.getSurname(),
                userEntity.getEnabled(),
                roles);
    }

    public static UserEntity toEntity(User user) {

        List<RoleEntity> roleEntities = user.getRoles().stream()
                .map(RoleEntityTransformer::toEntity)
                .collect(Collectors.toList());

        UserEntity userEntity = new UserEntity();
        userEntity.setId(user.getId());
        userEntity.setEmail(user.getEmail());
        userEntity.setPassword(user.getPassword());
        userEntity.setName(user.getName());
        userEntity.setSurname(user.getSurname());
        userEntity.setEnabled(user.getEnabled());
        userEntity.setRoles(roleEntities);

        return userEntity;
    }
}
