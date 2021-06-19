package com.javi.uned.pfgbackend.domain.ports.database;

import com.javi.uned.pfgbackend.domain.exceptions.EntityNotFound;
import com.javi.uned.pfgbackend.domain.user.model.Privilege;

import java.util.List;

public interface PrivilegeDAO {

    List<Privilege> findAll();

    Privilege findByExactName(String name) throws EntityNotFound;

    Privilege save(Privilege privilege);
}
