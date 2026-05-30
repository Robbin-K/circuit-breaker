package com.robbin.user_service;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    @GetMapping("/{id}")
    public Map<String, Object> getUser(
            @PathVariable Long id) {

        return Map.of(
                "id", id,
                "name", "Robbin"
        );
    }
}