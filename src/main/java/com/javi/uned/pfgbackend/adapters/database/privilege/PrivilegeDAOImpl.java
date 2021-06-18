package com.javi.uned.pfgbackend.adapters.database.privilege;

import com.javi.uned.pfgbackend.adapters.database.role.RoleEntity;
import com.javi.uned.pfgbackend.adapters.database.role.RoleEntityTransformer;
import com.javi.uned.pfgbackend.adapters.database.role.RoleRepository;
import com.javi.uned.pfgbackend.domain.ports.database.PrivilegeDAO;
import com.javi.uned.pfgbackend.domain.user.model.Privilege;
import com.javi.uned.pfgbackend.domain.user.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PrivilegeDAOImpl implements PrivilegeDAO {

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Override
    public List<Privilege> findAll() {

        List<PrivilegeEntity> privilegeEntities = privilegeRepository.findAll();

        return privilegeEntities.stream()
                .map(PrivilegeEntityTransformer::toDomainObject)
                .collect(Collectors.toList());
    }
}
