package com.javi.uned.pfgbackend.adapters.api.administration;

import com.javi.uned.pfgbackend.adapters.database.log.Log;
import com.javi.uned.pfgbackend.adapters.database.log.LogRepository;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@Api(tags = "Logs")
public class AdminControllerImpl implements AdminController {

    @Autowired
    private LogRepository logRepository;

    public ResponseEntity<Page<Log>> getLogs(int size, int index, boolean ascendent) {
        Sort sort = Sort.by(ascendent ? Sort.Direction.ASC : Sort.Direction.DESC, "id");
        PageRequest pageRequest = PageRequest.of(index, size, sort);
        Page<Log> logPage = logRepository.findAll(pageRequest);
        return ResponseEntity.ok(logPage);
    }
}
