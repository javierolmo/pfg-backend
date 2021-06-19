package com.javi.uned.pfgbackend.adapters.api.roles;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

public interface RoleController {

    @GetMapping(value = "/api/roles", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity findAllRoles();
}
