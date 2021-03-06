package com.javi.uned.pfgbackend.domain.user;

import com.javi.uned.pfgbackend.domain.exceptions.EntityNotFound;
import com.javi.uned.pfgbackend.domain.ports.database.PrivilegeDAO;
import com.javi.uned.pfgbackend.domain.user.model.Privilege;
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

    public Privilege createPrivilegeIfNotFound(String name) {
        try {
            return privilegeDAO.findByExactName(name);
        } catch (EntityNotFound entityNotFound) {
            Privilege privilege = new Privilege(null, name);
            return privilegeDAO.save(privilege);
        }
    }


}
