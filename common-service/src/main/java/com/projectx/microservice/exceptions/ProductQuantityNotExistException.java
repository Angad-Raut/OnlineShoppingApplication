package com.projectx.microservice.exceptions;

public class ProductQuantityNotExistException extends RuntimeException {
    public ProductQuantityNotExistException(String msg) {
        super(msg);
    }
}
