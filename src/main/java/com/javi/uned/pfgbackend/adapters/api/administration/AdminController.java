package com.javi.uned.pfgbackend.adapters.api.administration;

import com.javi.uned.pfgbackend.adapters.database.log.Log;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface AdminController {

    /**
     * Gets a page of logs
     * @param size number of elements of the page
     * @param index page number starting from 0
     * @param ascendent direction of order sorted by log id
     * @return
     */
    @GetMapping(value = "/api/admin/logs/page", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Page<Log>> getLogs(
            @RequestParam(defaultValue = "30", required = false) int size,
            @RequestParam(defaultValue = "0", required = false) int index,
            @RequestParam(defaultValue = "true", required = false) boolean ascendent);


}
