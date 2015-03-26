package com.sample.ecommerce;

import com.sample.ecommerce.service.ProductService;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration
public class Application {
    
    @Autowired
    private ProductService productService;
    
    @PostConstruct
    private void setup() {
        productService.deleteAll();
    }

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
