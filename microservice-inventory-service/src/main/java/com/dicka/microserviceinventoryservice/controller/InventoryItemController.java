package com.dicka.microserviceinventoryservice.controller;

import com.dicka.microserviceinventoryservice.entity.InventoryItem;
import com.dicka.microserviceinventoryservice.repository.InventoryItemRepository;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
public class InventoryItemController {

    private final InventoryItemRepository inventoryItemRepository;

    @Autowired
    public InventoryItemController(InventoryItemRepository inventoryItemRepository){
        this.inventoryItemRepository = inventoryItemRepository;
    }

    @GetMapping(value = "/api/inventory/{productCode}")
    @HystrixCommand
    public ResponseEntity<Optional<InventoryItem>> getInventoryByProductCode(@PathVariable("productCode") String productCode){
        log.info("Finding inventory by product code : "+productCode);
//        return inventoryItemRepository.findByProductCode(productCode)
//                .map(data -> {
//                    return new ResponseEntity<InventoryItem>(data, HttpStatus.OK);
//                })
//                .orElse(new ResponseEntity<InventoryItem>(HttpStatus.NOT_FOUND));

        Optional<InventoryItem> inventoryItem = inventoryItemRepository.findByProductCode(productCode);
        if (inventoryItem.isPresent()){
            return new ResponseEntity<Optional<InventoryItem>>(inventoryItem, HttpStatus.OK);
        }else {
            return new ResponseEntity<Optional<InventoryItem>>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/api/inventory")
    @HystrixCommand
    public List<InventoryItem> getInventory(){
        log.info("finding inventory for all products");
        return inventoryItemRepository.findAll();
    }
}
