package com.projectx.microservice.exceptions;

public class ProductNotVerifiedException extends RuntimeException {
    public ProductNotVerifiedException(String msg) {
        super(msg);
    }
}
