package com.dicka.microservicecatalog.service;

import com.dicka.microservicecatalog.entity.Product;
import com.dicka.microservicecatalog.model.ProductInventoryResponse;
import com.dicka.microservicecatalog.repository.ProductRepository;
import com.dicka.microservicecatalog.utils.MyThreadLocalsHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class ProductService {

   private final ProductRepository productRepository;
   private final InventoryClientService inventoryClientService;

   @Autowired
   public ProductService(ProductRepository productRepository, InventoryClientService inventoryClientService){
       this.productRepository = productRepository;
       this.inventoryClientService = inventoryClientService;
   }

   public List<Product> getAllProducts(){
       List<Product> products = productRepository.findAll();
       final Map<String, Integer> inventoryLevels = getInventoryLevelsWithFeignClient();
       final List<Product> availableProducts = products.stream()
               .filter(p -> inventoryLevels.get(p.getCode()) != null &&
                       inventoryLevels.get(p.getCode()) > 0)
               .collect(Collectors.toList());
       return availableProducts;
   }

   private Map<String, Integer> getInventoryLevelsWithFeignClient(){
       log.info("Fetching inventory level using FeignClient");
       Map<String, Integer> inventoryLevels = new HashMap<>();
       List<ProductInventoryResponse> inventoryResponses = inventoryClientService.getProductInventoryLevels();
       for (ProductInventoryResponse item : inventoryResponses){
            inventoryLevels.put(item.getProductCode(), item.getAvailableQuantity());
       }
       log.debug("InventoryLevels : {} "+inventoryLevels);
       return inventoryLevels;
   }

   public Optional<Product> findProductByCode(String code){
       Optional<Product> productOptional = productRepository.findByCode(code);
       if (productOptional.isPresent()){
           String correlationId = UUID.randomUUID().toString();
           MyThreadLocalsHolder.setCorelationId(correlationId);
           log.info("Before correlationId : "+MyThreadLocalsHolder.getCorrelationId());
           log.info("fetching inventory levels for product code : "+code);

           Optional<ProductInventoryResponse> itemResponseEntity =
                   this.inventoryClientService.getProductInventoryByCode(code);

           if (itemResponseEntity.isPresent()){
               Integer quantity = itemResponseEntity.get().getAvailableQuantity();
               productOptional.get().setInStock(quantity > 0);
           }
           log.info("After correlationID : "+MyThreadLocalsHolder.getCorrelationId());
       }
       return productOptional;
   }
}
