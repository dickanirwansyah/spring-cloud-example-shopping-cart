package com.dicka.microservicecatalog.controller;

import com.dicka.microservicecatalog.entity.Product;
import com.dicka.microservicecatalog.exception.ProductNotFound;
import com.dicka.microservicecatalog.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@Slf4j
@RequestMapping(value = "/api/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService){
        this.productService = productService;
    }

    @GetMapping
    public List<Product> allProduct(HttpServletRequest request){
        log.info("finding all products ");
        String auth_header = request.getHeader("AUTH_HEADER");
        log.info("AUTH_HEADER : "+auth_header);
        return productService.getAllProducts();
    }

    @GetMapping(value = "/{code}")
    public Product getProductByCode(@PathVariable("code") String productCode){
        log.info("finding product by code {} : "+productCode);
        return productService.findProductByCode(productCode)
                .orElseThrow(() ->
                        new ProductNotFound("Product with code ["+productCode+"] doesnt exists."));
    }
}
