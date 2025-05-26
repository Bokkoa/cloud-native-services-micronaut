package com.bokkoa.mnlearn.product;

public class ProductTypeNotFoundException extends RuntimeException {
    public ProductTypeNotFoundException(Exception e){
        super(e);
    }
}
