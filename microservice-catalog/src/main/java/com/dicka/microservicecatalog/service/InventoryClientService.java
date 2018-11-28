package com.dicka.microservicecatalog.service;

import com.dicka.microservicecatalog.model.ProductInventoryResponse;
import com.dicka.microservicecatalog.utils.MyThreadLocalsHolder;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class InventoryClientService {

    private final RestTemplate restTemplate;
    private final InventoryFeignClient inventoryFeignClient;
    //static path
    private static final String INVENTORY_PATH = "http://microservice-inventory-service/api/";

    @Autowired
    public InventoryClientService(RestTemplate restTemplate, InventoryFeignClient inventoryFeignClient){
        this.restTemplate = restTemplate;
        this.inventoryFeignClient = inventoryFeignClient;
    }

    /** hystrix list inventory **/
    @HystrixCommand(fallbackMethod = "getDefaultProductInventoryLevels",
    commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",
                    value = "10000")
    })
    public List<ProductInventoryResponse> getProductInventoryLevels(){
        return inventoryFeignClient.getInventoryLevels();
    }

    /** hystrix get product code **/
    @HystrixCommand(fallbackMethod = "getDefaultProductInventoryByCode")
    public Optional<ProductInventoryResponse> getProductInventoryByCode(String productCode){
        log.info("CorrelationID : "+MyThreadLocalsHolder.getCorrelationId());
        ResponseEntity<ProductInventoryResponse> itemResponseEntity =
                restTemplate.getForEntity(INVENTORY_PATH+"inventory/{productCode}",
                        ProductInventoryResponse.class,
                        productCode);

        if (itemResponseEntity.getStatusCode() == HttpStatus.OK){
            Integer quantity = itemResponseEntity.getBody().getAvailableQuantity();
            log.info("Available quantity : "+quantity);
            return Optional.ofNullable(itemResponseEntity.getBody());
        }else {
            log.error("unable to get inventory product code : "+productCode+" StatusCode : "
                    +itemResponseEntity.getStatusCode());
            return Optional.empty();
        }
    }

    /** default fallback list inventory **/
    @SuppressWarnings("unused")
    public List<ProductInventoryResponse> getDefaultProductInventoryLevels(){
        log.info("Returning default product inventory levels");
        return new ArrayList<>();
    }

    /** default fallback get product code **/
    @SuppressWarnings("unused")
    public Optional<ProductInventoryResponse> getDefaultProductInventoryByCode(String productCode){
        log.info("Returning default product inventory by product code : "+productCode);
        log.info("CorrelationID : "+ MyThreadLocalsHolder.getCorrelationId());

        ProductInventoryResponse response = new ProductInventoryResponse();
        response.setProductCode(productCode);
        response.setAvailableQuantity(50);
        return Optional.ofNullable(response);
    }
}
