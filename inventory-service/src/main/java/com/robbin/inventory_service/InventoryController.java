package com.robbin.inventory_service;

import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final Random random = new Random();

    @GetMapping("/{id}")
    public Map<String, Object> inventory(
            @PathVariable Long id) {

        if(random.nextInt(10) < 5) {
            throw new RuntimeException(
                    "Inventory failure"
            );
        }

        return Map.of(
                "productId", id,
                "available", true
        );
    }
}