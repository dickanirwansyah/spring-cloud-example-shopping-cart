package com.dicka.microservicecatalog.service;

import com.dicka.microservicecatalog.model.ProductInventoryResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "microservice-inventory-service")
public interface InventoryFeignClient {

    @GetMapping(value = "/api/inventory")
    public List<ProductInventoryResponse> getInventoryLevels();

    @GetMapping(value = "/api/inventory/{productCode}")
    public List<ProductInventoryResponse> getInventoryByProductCode(String productCode);
}
