package com.bokkoa.mnlearn;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
class HelloWorldControllerTest {

    private static final Logger LOG = LoggerFactory.getLogger(HelloWorldControllerTest.class);

    @Inject
    @Client("/")
    HttpClient httpClient;

    @Test
    void helloWorldEndpointResponseWithTextHelloWorld() {
        var response = httpClient.toBlocking().retrieve("/hello");
        LOG.trace("Response: {}", response);
        assertEquals("Hello from service!", response);
    }

    @Test
    void helloWorldEndpointRespondsWithStatusCode200(){
        var response = httpClient.toBlocking().exchange("/hello", String.class);
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals("Hello from service!", response.getBody().get());
    }

}
