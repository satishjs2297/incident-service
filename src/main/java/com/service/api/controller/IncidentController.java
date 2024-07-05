package com.service.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@RequestMapping("/incident")
@RestController
public class IncidentController {

    @GetMapping("/ids")
    public List<String> getIncidentIds() {
        return Arrays.asList("1","22","333","4444","55555");
    }

}
