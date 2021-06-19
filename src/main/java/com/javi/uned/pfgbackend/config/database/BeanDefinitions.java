package com.javi.uned.pfgbackend.config.database;

import com.javi.uned.pfgbackend.adapters.database.log.LogDAOImpl;
import com.javi.uned.pfgbackend.adapters.database.privilege.PrivilegeDAOImpl;
import com.javi.uned.pfgbackend.adapters.database.role.RoleDAOImpl;
import com.javi.uned.pfgbackend.adapters.database.sheet.SheetDAOImpl;
import com.javi.uned.pfgbackend.adapters.database.user.UserDAOImpl;
import com.javi.uned.pfgbackend.domain.ports.database.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanDefinitions {

    @Bean
    public UserDAO userDAO(UserDAOImpl userDAO) {
        return userDAO;
    }

    @Bean
    public RoleDAO roleDAO(RoleDAOImpl roleDAO) {
        return roleDAO;
    }

    @Bean
    public PrivilegeDAO privilegeDAO(PrivilegeDAOImpl privilegeDAO) {
        return privilegeDAO;
    }

    @Bean
    public LogDAO logDAO(LogDAOImpl logDAO) {
        return logDAO;
    }

    @Bean
    public SheetDAO sheetDAO(SheetDAOImpl sheetDAO) {
        return sheetDAO;
    }

}
