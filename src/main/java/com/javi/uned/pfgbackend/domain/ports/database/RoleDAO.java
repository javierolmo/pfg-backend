package com.javi.uned.pfgbackend.domain.ports.database;

import com.javi.uned.pfgbackend.domain.exceptions.EntityNotFound;
import com.javi.uned.pfgbackend.domain.user.model.Privilege;
import com.javi.uned.pfgbackend.domain.user.model.Role;

import java.util.Collection;
import java.util.List;

public interface RoleDAO {

    List<Role> findAll();

    Role findByName(String name) throws EntityNotFound;

    Role createRoleIfNotFound(String name, Collection<Privilege> privileges);
}
