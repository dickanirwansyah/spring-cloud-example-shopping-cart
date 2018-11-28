package com.dicka.microservicecatalog.model;

import lombok.Data;

@Data
public class ProductInventoryResponse {

    private String productCode;
    private Integer availableQuantity = 0;
}
