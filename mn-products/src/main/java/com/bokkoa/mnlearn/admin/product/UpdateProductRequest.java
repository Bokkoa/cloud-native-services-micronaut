package com.bokkoa.mnlearn.admin.product;

import com.bokkoa.mnlearn.product.Product;
import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record UpdateProductRequest (
        String name,
        Product.Type type
) {
}
