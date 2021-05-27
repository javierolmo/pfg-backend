package com.javi.uned.pfgweb.config;

import com.javi.uned.pfgweb.beans.Privilege;
import com.javi.uned.pfgweb.beans.Role;
import com.javi.uned.pfgweb.beans.User;
import com.javi.uned.pfgweb.repositories.PrivilegeRepository;
import com.javi.uned.pfgweb.repositories.RoleRepository;
import com.javi.uned.pfgweb.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

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

        if (alreadySetup)
            return;
        Privilege readPrivilege = createPrivilegeIfNotFound("READ_PRIVILEGE");
        Privilege writePrivilege = createPrivilegeIfNotFound("WRITE_PRIVILEGE");

        List<Privilege> adminPrivileges = Arrays.asList(readPrivilege, writePrivilege);
        createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);
        createRoleIfNotFound("ROLE_USER", Arrays.asList(readPrivilege));

        if(!userService.existsByEmail("olmo.injerto.javier@gmail.com")){
            Role adminRole = roleRepository.findByName("ROLE_ADMIN");
            Collection<Role> roles = Arrays.asList(adminRole);
            User user = new User();
            user.setEmail("olmo.injerto.javier@gmail.com");
            user.setPassword("5885");
            user.setName("Javier");
            user.setSurname("Olmo");
            user.setEnabled(true);
            user.setRoles(roles);
            userService.createUser(user);
        }

        if(!userService.existsByEmail("tester@gmail.com")){
            Role userRole = roleRepository.findByName("ROLE_USER");
            Collection<Role> roles = Arrays.asList(userRole);
            User user = new User();
            user.setEmail("tester@gmail.com");
            user.setPassword("5885");
            user.setName("TesterName");
            user.setSurname("TesterSurname");
            user.setEnabled(true);
            user.setRoles(roles);
            userService.createUser(user);
        }

        alreadySetup = true;
    }

    @Transactional
    private Privilege createPrivilegeIfNotFound(String name) {
        Privilege privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = new Privilege();
            privilege.setName(name);
            privilegeRepository.save(privilege);
        }
        return privilege;
    }

    @Transactional
    private Role createRoleIfNotFound(String name, Collection<Privilege> privileges) {
        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role();
            role.setName(name);
            role.setPrivileges(privileges);
            roleRepository.save(role);
        }
        return role;
    }
}
