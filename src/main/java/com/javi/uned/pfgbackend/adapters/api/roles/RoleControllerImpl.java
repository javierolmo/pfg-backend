package com.javi.uned.pfgbackend.adapters.api.roles;

import com.javi.uned.pfgbackend.domain.user.RoleService;
import com.javi.uned.pfgbackend.domain.user.model.Role;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@Api(tags = "Roles")
public class RoleControllerImpl implements RoleController {

    @Autowired
    private RoleService roleService;

    @Override
    public ResponseEntity findAllRoles() {

        List<Role> roles = roleService.findAll();
        List<RoleDTO> roleDTOs = roles.stream().map(RoleDTOTransformer::toTransferObject).collect(Collectors.toList());

        return ResponseEntity.ok(roleDTOs);
    }
}
