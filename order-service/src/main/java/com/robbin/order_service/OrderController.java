package com.robbin.order_service;

import com.robbin.order_service.circuitbreaker.InventoryCircuitBreaker;
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
    private final InventoryCircuitBreaker breaker;

    String inventory = "Inventory Service DOWN"; // can set any default value
    String user = "User Service DOWN";  // can set any default value

    @GetMapping("/order")
    public String order() {

        String user = "USER DOWN";
        String inventory = "INVENTORY DOWN";

        String inventoryHealth =
                redisTemplate.opsForValue()
                        .get("health:inventory-service");

        String userHealth =
                redisTemplate.opsForValue()
                        .get("health:user-service");

        try {

            if ("UP".equals(userHealth)) {

                user = restTemplate.getForObject(
                        userServiceUrl + "/users/1",
                        String.class
                );
            }

        } catch (Exception ex) {

            user = "USER SERVICE FAILURE";
        }

        try {

            if (!breaker.allowRequest()) {

                inventory = "CIRCUIT OPEN";

            } else if ("UP".equals(inventoryHealth)) {

                inventory = restTemplate.getForObject(
                        inventoryServiceUrl + "/inventory/101",
                        String.class
                );

                breaker.recordSuccess();
            }

        } catch (Exception ex) {

            breaker.recordFailure();

            inventory = "INVENTORY FAILURE";
        }

        return """
            USER:
            %s

            INVENTORY:
            %s
            """.formatted(user, inventory);
    }
    
    @GetMapping("/circuit-state")
    public String circuitState() {
        return breaker.getState().name();
    }
}