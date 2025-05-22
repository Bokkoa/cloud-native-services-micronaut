package com.bokkoa.mnlearn.hello;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import jakarta.inject.Inject;

@Controller("/hello")
public class HelloWorldController {


    private final BasicService service;

    public HelloWorldController(final BasicService service) {
        this.service = service;
    }

    @Get(produces = MediaType.TEXT_PLAIN)
    public String helloworld(){
        return service.helloFromService();
    }
}
