package com.javi.uned.pfgbackend.domain.ports.database;

import com.javi.uned.pfgbackend.domain.user.model.Role;

import java.util.List;

public interface RoleDAO {

    List<Role> findAll();

}
