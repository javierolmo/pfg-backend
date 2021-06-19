package com.javi.uned.pfgbackend.adapters.database.privilege;

import com.javi.uned.pfgbackend.domain.exceptions.EntityNotFound;
import com.javi.uned.pfgbackend.domain.ports.database.PrivilegeDAO;
import com.javi.uned.pfgbackend.domain.user.model.Privilege;
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

    @Override
    public Privilege findByExactName(String name) throws EntityNotFound {
        PrivilegeEntity privilegeEntity = privilegeRepository.findByName(name);
        if (privilegeEntity == null) {
            throw new EntityNotFound("Could not find privilege with name '"+name+"'");
        } else {
            return PrivilegeEntityTransformer.toDomainObject(privilegeEntity);
        }
    }

    @Override
    public Privilege save(Privilege privilege) {
        PrivilegeEntity privilegeEntity = PrivilegeEntityTransformer.toEntity(privilege);
        privilegeEntity = privilegeRepository.save(privilegeEntity);
        return PrivilegeEntityTransformer.toDomainObject(privilegeEntity);
    }
}
