package com.bokkoa.mnlearn;

import com.bokkoa.mnlearn.admin.product.UpdateProductRequest;
import com.bokkoa.mnlearn.auth.jwt.JWTClient;
import com.bokkoa.mnlearn.product.Product;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.security.authentication.UsernamePasswordCredentials;
import io.micronaut.security.token.render.BearerAccessRefreshToken;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
public class AdminProductsControllerTest {

    final static Logger LOG = LoggerFactory.getLogger(AdminProductsControllerTest.class);
    @Inject
    @Client("/admin/products")
    HttpClient client;

    @Inject
    @Client("/") // Root of the app
    JWTClient jwtClient;

    @Inject
    InMemoryStore store;


    String bearerPrefix = "Bearer ";


    @Test
    void aNewProductCanBeAddedUsingTheAdminPostEndpoint(){

        var token = startSession();

        var productToAdd = new Product(1234, "my-test-product", Product.Type.OTHER);

        store.getProducts().remove(productToAdd.id());

        assertNull(store.getProducts().get(productToAdd.id()));


     /*
        // CONVENTIONAL CLIENT
        var response = client.toBlocking().exchange(
                HttpRequest.POST("/", productToAdd).accept(MediaType.APPLICATION_JSON).bearerAuth(token.getAccessToken()),
                Product.class
        );

        assertEquals(HttpStatus.CREATED, response.getStatus());
        assertTrue(response.getBody().isPresent());
        assertEquals(productToAdd.id(), response.getBody().get().id());
        assertEquals(productToAdd.name(), response.getBody().get().name());
        assertEquals(productToAdd.type(), response.getBody().get().type());

        */

        // CUSTOM REST CLIENT
        final HttpResponse<Product> response = jwtClient.addNewProduct(bearerPrefix + token.getAccessToken(), productToAdd);
        assertEquals(HttpStatus.CREATED, response.getStatus());
        assertTrue(response.getBody().isPresent());
        assertEquals(productToAdd.id(), response.getBody().get().id());
        assertEquals(productToAdd.name(), response.getBody().get().name());
        assertEquals(productToAdd.type(), response.getBody().get().type());


    }

    @Test
    void addingAProductTwiceResultsInConflict(){
        var token = startSession();
        var productToAdd = new Product(1234, "my-test-product", Product.Type.OTHER);

        store.getProducts().remove(productToAdd.id());
        assertNull(store.getProducts().get(productToAdd.id()));

        var response = client.toBlocking().exchange(
                HttpRequest.POST("/", productToAdd).accept(MediaType.APPLICATION_JSON).bearerAuth(token.getAccessToken()),
                Product.class
        );

        assertEquals(HttpStatus.CREATED, response.getStatus());

        var expectedConflict = assertThrows(HttpClientResponseException.class,
                () -> client.toBlocking().exchange(HttpRequest.POST("/", productToAdd).accept(MediaType.APPLICATION_JSON).bearerAuth(token.getAccessToken())));

        assertEquals(HttpStatus.CONFLICT, expectedConflict.getStatus());

    }


    @Test
    void aProductCanBeUpdatedUsingTheAdminPutEndpoint(){
        var token = startSession();
        var productToUpdate = new Product(999, "old-value", Product.Type.OTHER);

        store.getProducts().put(productToUpdate.id(), productToUpdate);
        assertEquals(productToUpdate, store.getProducts().get(productToUpdate.id()));


        var updateRequest = new UpdateProductRequest("new-value", Product.Type.TEA);

        /*

        // CONVENTIONAL CLIENT

        var response = client.toBlocking().exchange(
                HttpRequest.PUT("/" + productToUpdate.id(), updateRequest).accept(MediaType.APPLICATION_JSON).bearerAuth(token.getAccessToken()),
                Product.class
        );

        assertEquals(HttpStatus.OK, response.getStatus());

        var productFromStore = store.getProducts().get(productToUpdate.id());
        assertEquals(updateRequest.name(), productFromStore.name());
        assertEquals(updateRequest.type(), productFromStore.type());

        */


        // CUSTOM REST CLIENT
        final HttpResponse<Product> response = jwtClient.updateProduct(bearerPrefix + token.getAccessToken(), productToUpdate.id(), updateRequest);

        assertEquals(HttpStatus.OK, response.getStatus());

        var productFromStore = store.getProducts().get(productToUpdate.id());
        assertEquals(updateRequest.name(), productFromStore.name());
        assertEquals(updateRequest.type(), productFromStore.type());
    }

    @Test
    void aNonExistingProductWillBeAddedWhenUsingTheAdminPutEndpoint(){

        var token = startSession();
        var productId = 999;
        store.getProducts().remove(productId);

        assertNull(store.getProducts().get(productId));

        var updateRequest = new UpdateProductRequest("new-value", Product.Type.TEA);

        var response = client.toBlocking().exchange(
          HttpRequest.PUT("/" + productId, updateRequest).accept(MediaType.APPLICATION_JSON).bearerAuth(token.getAccessToken()),
          Product.class
        );


        assertEquals(HttpStatus.OK, response.getStatus());
        var productFromStore = store.getProducts().get(productId);

        assertEquals(updateRequest.name(), productFromStore.name());
        assertEquals(updateRequest.type(), productFromStore.type());
    }

    @Test
    void aProductCanBeDeletedUsingTheAdminDeleteEndpoint(){
        var productToDelete = new Product(987, "delete-me", Product.Type.OTHER);
        var token = startSession();
        store.addProduct(productToDelete);


        assertTrue(store.getProducts().containsKey(productToDelete.id()));
        assertTrue(store.getProducts().containsValue(productToDelete));


      /*

      // CONVENTIONAL CLIENT

      final HttpResponse<Product> response = client.toBlocking().exchange(
                HttpRequest.DELETE("/" + productToDelete.id()).accept(MediaType.APPLICATION_JSON).bearerAuth(token.getAccessToken()),
                Argument.of(Product.class)
        );

        assertEquals(HttpStatus.OK, response.getStatus());
        assertTrue(response.getBody().isPresent());
        assertEquals(productToDelete.id(), response.getBody().get().id());
        assertEquals(productToDelete.name(), response.getBody().get().name());
        assertEquals(productToDelete.type(), response.getBody().get().type());

        */

        // CUSTOM REST CLIENT
        final HttpResponse<Product> response = jwtClient.deleteProduct(bearerPrefix + token.getAccessToken(), productToDelete.id());

        assertEquals(HttpStatus.OK, response.getStatus());
        assertTrue(response.getBody().isPresent());
        assertEquals(productToDelete.id(), response.getBody().get().id());
        assertEquals(productToDelete.name(), response.getBody().get().name());
        assertEquals(productToDelete.type(), response.getBody().get().type());
    }

    @Test
    void deletingANonExistingProductResultsInNotFoundResponse(){
        var productId = 987;
        var token = startSession();
        store.deleteProduct(productId);
        assertNull(store.getProducts().get(productId));

        var response = assertThrows(HttpClientResponseException.class,
                () -> client.toBlocking().exchange(
                        HttpRequest.DELETE("/" + productId).accept(MediaType.APPLICATION_JSON).bearerAuth(token.getAccessToken())
                ));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
    }

    BearerAccessRefreshToken startSession(){
        final UsernamePasswordCredentials credentials = new UsernamePasswordCredentials("my-user", "secret");

        final BearerAccessRefreshToken token = jwtClient.login(credentials);

        assertNotNull(token);
        assertEquals("my-user", token.getUsername());

        LOG.debug("Login Bearer Token: {} expires in {}", token.getAccessToken(), token.getExpiresIn());

        return token;

    }
}
