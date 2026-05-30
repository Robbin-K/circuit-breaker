package com.robbin.order_service;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;


@RestController
@RequiredArgsConstructor
public class OrderController {

    private final RestTemplate restTemplate;

    @GetMapping("/order")
    public String order() {

        String user =
                restTemplate.getForObject(
                        "http://localhost:8080/users/1",
                        String.class
                );

        String inventory =
                restTemplate.getForObject(
                        "http://localhost:8082/inventory/101",
                        String.class
                );

        return """
                USER:
                %s

                INVENTORY:
                %s
                """.formatted(user, inventory);
    }
}