package com.bokkoa.mnlearn.hello;

import io.micronaut.context.annotation.Primary;
import jakarta.inject.Singleton;

@Primary
@Singleton
public class HelloWorldService implements BasicService{

    @Override
    public String helloFromService(){
        return "Hello from service!";
    }


}
