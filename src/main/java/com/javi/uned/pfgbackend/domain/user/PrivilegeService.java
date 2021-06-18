package com.javi.uned.pfgbackend.domain.user;

import com.javi.uned.pfgbackend.domain.ports.database.PrivilegeDAO;
import com.javi.uned.pfgbackend.domain.ports.database.RoleDAO;
import com.javi.uned.pfgbackend.domain.user.model.Privilege;
import com.javi.uned.pfgbackend.domain.user.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrivilegeService {

    @Autowired
    private PrivilegeDAO privilegeDAO;

    public List<Privilege> findAll() {
        return privilegeDAO.findAll();
    }


}
