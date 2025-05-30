package com.bokkoa.mnlearn;

import com.bokkoa.mnlearn.hello.HelloMicronautService;
import com.bokkoa.mnlearn.hello.HelloWorldService;
import io.micronaut.runtime.Micronaut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application {

    private static final Logger LOG = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        var context = Micronaut.run(Application.class, args);

        LOG.debug("Message from service: {}",
            context.getBean(HelloWorldService.class).helloFromService()
        );

        LOG.debug("Message from service: {}",
            context.getBean(HelloMicronautService.class).helloFromService()
        );
    }
}