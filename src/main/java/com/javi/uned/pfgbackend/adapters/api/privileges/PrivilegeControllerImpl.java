package com.javi.uned.pfgbackend.adapters.api.privileges;

import com.javi.uned.pfgbackend.domain.user.PrivilegeService;
import com.javi.uned.pfgbackend.domain.user.model.Privilege;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@Api(tags = "Privileges")
public class PrivilegeControllerImpl implements PrivilegeController {

    @Autowired
    private PrivilegeService privilegeService;

    @Override
    public ResponseEntity findAllPrivileges() {

        List<Privilege> privileges = privilegeService.findAll();
        List<PrivilegeDTO> privilegeDTOs = privileges.stream()
                .map(PrivilegeDTOTransformer::toTransferObject)
                .collect(Collectors.toList());

        return ResponseEntity.ok(privilegeDTOs);
    }
}
