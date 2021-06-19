package com.javi.uned.pfgbackend.domain.user;

import com.javi.uned.pfgbackend.domain.exceptions.EntityNotFound;
import com.javi.uned.pfgbackend.domain.ports.database.RoleDAO;
import com.javi.uned.pfgbackend.domain.user.model.Privilege;
import com.javi.uned.pfgbackend.domain.user.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class RoleService {

    @Autowired
    private RoleDAO roleDAO;

    public List<Role> findAll() {
        return roleDAO.findAll();
    }

    public Role findByName(String name) throws EntityNotFound {
        return roleDAO.findByName(name);
    }

    public Role createRoleIfNotFound(String name, Collection<Privilege> privileges) {
        return roleDAO.createRoleIfNotFound(name, privileges);
    }


}
