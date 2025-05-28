package com.bokkoa.mnlearn.product;

import com.bokkoa.mnlearn.InMemoryStore;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Error;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.http.hateoas.JsonError;
import io.micronaut.http.hateoas.Link;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/products")
public class ProductsController {

    private static final Logger LOG = LoggerFactory.getLogger(ProductsController.class);
    private final InMemoryStore store;

    public  ProductsController(InMemoryStore store){
        this.store = store;
    }

    @Get
    public List<Product> listAllProducts() {
        return new ArrayList<>(store.getProducts().values());
    }

    @Get("{id}")
    public Product getProduct(@PathVariable Integer id){
        return store.getProducts().get(id);
    }

    @Get("/filter{?max,offset}")
    public List<Product> filteredProducts(
            @QueryValue Optional<Integer> max,
            @QueryValue Optional<Integer> offset
            ){

        return store.getProducts()
                    .values()
                    .stream()
                    .skip(offset.orElse(0))
                    .limit(max.orElse(0))
                    .toList();
    }

    @Get("/type/{type}")
    public List<Product> getProductByType(@PathVariable String type) {

        try {

        var productType = Product.Type.valueOf(type);

        return store.getProducts().values()
                .stream().filter(p -> productType.equals(p.type()))
                .toList();
        } catch (IllegalArgumentException e){
            throw new ProductTypeNotFoundException(e);
        }
    }

    @Error
    // It only chatches IllegalArgumentExceptions
    public HttpResponse<JsonError> productNotFound(HttpRequest<?> request, ProductTypeNotFoundException e){
        LOG.debug("Local error handler...");

        var error = new JsonError(
                "Invalid Product Type. Supported types are: " + Arrays.toString(Product.Type.values())
        ).link(Link.SELF, Link.of(request.getUri()));

        return HttpResponse.status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

}
