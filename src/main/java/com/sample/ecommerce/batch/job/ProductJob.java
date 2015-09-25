/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sample.ecommerce.batch.job;

import com.sample.ecommerce.batch.mapper.ProductMapper;
import com.sample.ecommerce.batch.writer.ProductWriter;
import com.sample.ecommerce.domain.Product;
import java.util.Map;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
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
        
        FlatFileItemReader<Map<String,Object>> productreader = new FlatFileItemReader<>();
        productreader.setResource(new ClassPathResource("data/products.csv"));
        productreader.setLineMapper(new DefaultLineMapper<Map<String,Object>>() {{
            setLineTokenizer(new DelimitedLineTokenizer());
            setFieldSetMapper(new ProductMapper());
        }});
        
        Step step1 = stepBuilderFactory.get("step1")
                .<Map<String,Object>, Map<String,Object>> chunk(10)
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
