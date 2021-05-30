package com.javi.uned.pfgbackend.rest;

import com.javi.uned.pfgbackend.beans.Log;
import com.javi.uned.pfgbackend.repositories.LogRepository;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/logs")
@Api(tags = "Logs")
public class LogREST {

    @Autowired
    private LogRepository logRepository;

    @GetMapping
    public List<Log> getLogs() {
        return logRepository.findAll();
    }
}
