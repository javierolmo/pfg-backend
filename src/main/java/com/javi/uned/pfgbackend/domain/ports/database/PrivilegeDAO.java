package com.javi.uned.pfgbackend.domain.ports.database;

import com.javi.uned.pfgbackend.domain.user.model.Privilege;

import java.util.List;

public interface PrivilegeDAO {

    List<Privilege> findAll();
}
