package com.dicka.microservicecatalog.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ProductNotFound extends RuntimeException {

    public ProductNotFound(String msg, Throwable cause){
        super(msg, cause);
    }

    public ProductNotFound(String msg){
        super(msg);
    }

    public ProductNotFound(Throwable cause){
        super(cause);
    }
}
