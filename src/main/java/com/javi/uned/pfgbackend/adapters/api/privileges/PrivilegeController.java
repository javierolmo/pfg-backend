package com.javi.uned.pfgbackend.adapters.api.privileges;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

public interface PrivilegeController {

    @GetMapping(value = "/api/privileges", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity findAllPrivileges();

}
