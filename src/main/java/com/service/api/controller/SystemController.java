package com.service.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.TreeMap;

@RestController
public class SystemController {


    @GetMapping("/env")
    public Map<String, String> getEnv() {
        return new TreeMap<>(System.getenv());
    }
}
