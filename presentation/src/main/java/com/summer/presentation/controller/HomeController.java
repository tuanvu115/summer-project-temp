package com.summer.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HomeController {

    @GetMapping
    public ResponseEntity<?> home() {
        Map<String,String> map = new HashMap<>();
        map.put("message", "Welcome to the Summer Application!");
        map.put("desc", "This is the posting transaction service.");
        return ResponseEntity.ok(map);
    }

}
