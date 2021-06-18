package com.javi.uned.pfgbackend.config.database;

import com.javi.uned.pfgbackend.adapters.database.privilege.PrivilegeEntity;
import com.javi.uned.pfgbackend.adapters.database.privilege.PrivilegeRepository;
import com.javi.uned.pfgbackend.adapters.database.role.RoleEntity;
import com.javi.uned.pfgbackend.adapters.database.role.RoleRepository;
import com.javi.uned.pfgbackend.domain.exceptions.ExistingUserException;
import com.javi.uned.pfgbackend.domain.exceptions.ValidationException;
import com.javi.uned.pfgbackend.domain.user.UserService;
import com.javi.uned.pfgbackend.domain.user.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private final Logger logger = LoggerFactory.getLogger(SetupDataLoader.class);
    boolean alreadySetup = false;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Autowired
    private UserService userService;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (alreadySetup) return;

        // Declare and create privileges
        PrivilegeEntity readLogsPrivilegeEntity = createPrivilegeIfNotFound("READ_LOGS");
        PrivilegeEntity disableUsersPrivilegeEntity = createPrivilegeIfNotFound("DISABLE_USERS");
        PrivilegeEntity[] privilegeEntities = new PrivilegeEntity[]{readLogsPrivilegeEntity, disableUsersPrivilegeEntity};

        // Declare and create roles
        RoleEntity adminRoleEntity = createRoleIfNotFound("ROLE_ADMIN", Arrays.asList(readLogsPrivilegeEntity, disableUsersPrivilegeEntity));
        RoleEntity userRoleEntity = createRoleIfNotFound("ROLE_USER", new ArrayList<>());
        RoleEntity[] roleEntities = new RoleEntity[]{adminRoleEntity, userRoleEntity};


        // Declare users
        User javi = new User(null, "jolmo60@alumno.uned.es", "1234", "Javier", "Olmo Injerto", true, new ArrayList<>());
        User tester = new User(null, "tester@gmail.com", "1234", "Tester", "Appelido1 Apellido2", true, new ArrayList<>());
        User joseManuel = new User(null, "jmcuadra@dia.uned.es", "1234", "Jose Manuel", "Cuadra Troncoso", true, new ArrayList<>());
        User[] users = new User[]{javi, tester, joseManuel};

        // Create users
        for (User user : users) {
            try {
                userService.registerUser(user);
            } catch (ValidationException | ExistingUserException e) {
                logger.debug("Omitting user creation: " + user.getEmail(), e);
            }
        }

        alreadySetup = true;
    }

    @Transactional
    private PrivilegeEntity createPrivilegeIfNotFound(String name) {
        PrivilegeEntity privilegeEntity = privilegeRepository.findByName(name);
        if (privilegeEntity == null) {
            privilegeEntity = new PrivilegeEntity();
            privilegeEntity.setName(name);
            privilegeRepository.save(privilegeEntity);
        }
        return privilegeEntity;
    }

    @Transactional
    private RoleEntity createRoleIfNotFound(String name, Collection<PrivilegeEntity> privilegeEntities) {
        RoleEntity roleEntity = roleRepository.findByName(name);
        if (roleEntity == null) {
            roleEntity = new RoleEntity();
            roleEntity.setName(name);
            roleEntity.setPrivilegeEntities(privilegeEntities);
            roleRepository.save(roleEntity);
        }
        return roleEntity;
    }
}
