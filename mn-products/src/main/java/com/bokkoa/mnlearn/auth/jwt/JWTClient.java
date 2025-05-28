package com.bokkoa.mnlearn.auth.jwt;

import com.bokkoa.mnlearn.admin.product.UpdateProductRequest;
import com.bokkoa.mnlearn.product.Product;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.security.authentication.UsernamePasswordCredentials;
import io.micronaut.security.token.render.BearerAccessRefreshToken;

@Client("/")
public interface JWTClient {

    @Post("/login") BearerAccessRefreshToken login(@Body UsernamePasswordCredentials credentials);


    @Post("/admin/products")
    HttpResponse<Product> addNewProduct(@Header String authorization, @Body Product product);

    @Put("/admin/products/{id}")
    HttpResponse<Product> updateProduct(@Header String authorization, @PathVariable Integer id, @Body UpdateProductRequest request);

    @Delete("/admin/products/{id}")
    HttpResponse<Product> deleteProduct(@Header String authorization, @PathVariable Integer id);
}
