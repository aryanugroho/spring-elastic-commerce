package com.sample.ecommerce.test;

import com.sample.ecommerce.BatchConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(value = {BatchConfiguration.class})
@ComponentScan(basePackages = {"com.sample.ecommerce.repositories","com.sample.ecommerce.service"})
public class TestConfiguration {

    
}
