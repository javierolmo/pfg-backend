package com.javi.uned.pfgbackend.rest;

import com.javi.uned.pfgbackend.beans.Log;
import com.javi.uned.pfgbackend.repositories.LogRepository;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/admin")
@Api(tags = "Logs")
public class AdminREST {

    @Autowired
    private LogRepository logRepository;

    @GetMapping("logs")
    public Page<Log> getLogs(
            @RequestParam(defaultValue = "30", required = false) int size,
            @RequestParam(defaultValue = "0", required = false) int index,
            @RequestParam(defaultValue = "true", required = false) boolean ascendent
    ) {
        Sort sort = Sort.by(ascendent? Sort.Direction.ASC: Sort.Direction.DESC, "id");
        return logRepository.findAll(PageRequest.of(index, size, sort));
    }
}
