package com.robbin.inventory_service;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @GetMapping("/{id}")
    public Map<String, Object> getInventory(
            @PathVariable Long id) {

        return Map.of(
                "productId", id,
                "available", true
        );
    }
}