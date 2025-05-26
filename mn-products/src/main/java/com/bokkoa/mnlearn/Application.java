package com.bokkoa.mnlearn;

import io.micronaut.runtime.Micronaut;

import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.info.*;

@OpenAPIDefinition(
     info = @Info(
         title = "Micronaut Products Microservice",
         version = "${my.api.version}",
         description = "${my.api.description}",
         license = @License(name = "MIT")
     )
)
public class Application {

    public static void main(String[] args) {
        Micronaut.run(Application.class, args);
    }
}