package com.robbin.order_service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


@RestController
@RequiredArgsConstructor
public class OrderController {

    @Value("${user.service.url}")
    private String userServiceUrl;

    @Value("${inventory.service.url}")
    private String inventoryServiceUrl;
    
    private final RestTemplate restTemplate;

    @GetMapping("/order")
    public String order() {

        String user =
                restTemplate.getForObject(
                        userServiceUrl + "/users/1",
                        String.class
                );

        String inventory =
                restTemplate.getForObject(
                        inventoryServiceUrl + "/inventory/101",
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