package com.sample.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerInitializedEvent;
import org.springframework.context.ApplicationListener;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        
        SpringApplication springApplication
                = new SpringApplication(Application.class);
        springApplication.addListeners(new ApplicationListener<EmbeddedServletContainerInitializedEvent>() {
            @Override
            public void onApplicationEvent(EmbeddedServletContainerInitializedEvent e) {
                System.out.println("event " + e.getClass().getName());
            }
        });
        springApplication.run(args);
    }

}
