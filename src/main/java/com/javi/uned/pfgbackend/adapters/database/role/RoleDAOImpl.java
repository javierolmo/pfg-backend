package com.javi.uned.pfgbackend.adapters.database.role;

import com.javi.uned.pfgbackend.domain.ports.database.RoleDAO;
import com.javi.uned.pfgbackend.domain.user.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RoleDAOImpl implements RoleDAO {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public List<Role> findAll() {

        List<RoleEntity> roleEntities = roleRepository.findAll();

        return roleEntities.stream()
                .map(RoleEntityTransformer::toDomainObject)
                .collect(Collectors.toList());
    }

}
