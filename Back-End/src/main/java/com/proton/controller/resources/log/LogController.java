package com.proton.controller.resources.log;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proton.models.entities.Log;
import com.proton.models.repositories.LogRepository;

@RestController
@RequestMapping(value = "/log")
public class LogController {

    @Autowired
    private LogRepository logRepository;

   @GetMapping
    public ResponseEntity<List<Log>> findAll(){
        List<Log> list = logRepository.findAll();
        return ResponseEntity.ok().body(list);        
    }
}

