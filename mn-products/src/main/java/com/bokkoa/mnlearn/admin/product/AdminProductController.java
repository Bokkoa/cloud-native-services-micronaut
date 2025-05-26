package com.bokkoa.mnlearn.admin.product;

import com.bokkoa.mnlearn.InMemoryStore;
import com.bokkoa.mnlearn.product.Product;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.http.exceptions.HttpStatusException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Admin")
@Controller("/admin/products")
public class AdminProductController {

    private final InMemoryStore store;

    public AdminProductController(InMemoryStore store) { this.store = store; }

    @Status(HttpStatus.CREATED)
    @Post(
            consumes = MediaType.APPLICATION_JSON,
            produces = MediaType.APPLICATION_JSON
    )
    @Operation(
            summary = "Creates a new Product.",
            description = "Accepts a product in the request  and persists it in the InMemoryStore"
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = Product.class))
    )
    @ApiResponse(
            responseCode = "409",
            description = "Product does already exists. The ID has to be unique."
    )
    @Tag(name = "Admin")
    public Product addNewProduct(@Body Product product){
        if(store.getProducts().containsKey(product.id())){
            throw  new HttpStatusException(
                    HttpStatus.CONFLICT,
                    "Product with id " + product.id() + " already exists"
            );
        }
        return store.addProduct(product);
    }

    @Put("{id}")
    public Product updateProduct(
            @PathVariable Integer id,
            @Body UpdateProductRequest request) {

        var updatedProduct = new Product(id, request.name(), request.type());

        return store.addProduct(updatedProduct);
    }

    @Delete("{id}")
    public Product deleteProduct(
            @PathVariable Integer id
    ){
        return store.deleteProduct(id);
    }
}
