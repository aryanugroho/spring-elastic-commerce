package com.sample.ecommerce.test;

import com.sample.ecommerce.BatchConfiguration;
import com.sample.ecommerce.ElasticSearchConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(value = {ElasticSearchConfiguration.class,BatchConfiguration.class})
@ComponentScan(basePackages = {"com.sample.ecommerce.repositories","com.sample.ecommerce.service"})
public class TestConfiguration {

    
}
