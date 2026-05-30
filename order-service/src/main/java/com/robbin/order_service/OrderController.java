package com.robbin.order_service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
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
    private final StringRedisTemplate redisTemplate;
    String inventory = "Inventory Service DOWN"; // can set any default value
    String user = "User Service DOWN";  // can set any default value

    @GetMapping("/order")
    public String order() {

        String inventoryHealth = redisTemplate.opsForValue().get("health:inventory-service");
        String userHealth = redisTemplate.opsForValue().get("health:user-service");


        if ("UP".equals(inventoryHealth)) {
            inventory = restTemplate.getForObject(inventoryServiceUrl + "/inventory/101", String.class);
        }
        if ("UP".equals(userHealth)) {
            user = restTemplate.getForObject(userServiceUrl + "/users/1", String.class);
        }

        return """
                USER:
                %s
                
                INVENTORY:
                %s
                """.formatted(user, inventory);
    }
}