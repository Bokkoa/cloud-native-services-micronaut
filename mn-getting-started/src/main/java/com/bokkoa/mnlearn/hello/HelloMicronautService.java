package com.bokkoa.mnlearn.hello;

import io.micronaut.context.annotation.Primary;
import jakarta.inject.Singleton;

@Singleton
public class HelloMicronautService implements BasicService{

    @Override
    public String helloFromService(){
        return "Hello Micronaut!";
    }


}
