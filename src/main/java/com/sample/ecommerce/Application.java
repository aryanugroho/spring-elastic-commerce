package com.sample.ecommerce;

import com.sample.ecommerce.service.CategoryService;
import com.sample.ecommerce.service.ProductService;
import com.sample.ecommerce.service.TermService;
import javax.annotation.PostConstruct;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@EnableBatchProcessing
@Configuration
public class Application {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private TermService termService;

    @PostConstruct
    private void setup() {
        productService.deleteAll();
        categoryService.deleteAll();
        termService.deleteAll();
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }

}
