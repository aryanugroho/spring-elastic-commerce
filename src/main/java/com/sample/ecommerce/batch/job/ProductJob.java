/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sample.ecommerce.batch.job;

import com.sample.ecommerce.batch.writer.ProductWriter;
import com.sample.ecommerce.domain.Product;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class ProductJob {
    
    @Autowired
    private ProductWriter productWriter;    

    // tag::jobstep[]
    @Bean
    public Job importProductsJob(JobBuilderFactory jobs,StepBuilderFactory stepBuilderFactory) {
        
        FlatFileItemReader<Product> productreader = new FlatFileItemReader<>();
        productreader.setResource(new ClassPathResource("data/products.csv"));
        productreader.setLineMapper(new DefaultLineMapper<Product>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setNames(new String[] { "id", "title","brand","category" });
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<Product>() {{
                setTargetType(Product.class);
            }});
        }});
        
        Step step1 = stepBuilderFactory.get("step1")
                .<Product, Product> chunk(10)
                .reader(productreader)                
                .writer(productWriter)
                .build();
        
        return jobs.get("importProductsJob")
                .incrementer(new RunIdIncrementer())
                .flow(step1)
                .end()
                .build();
    }

  
}
