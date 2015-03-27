/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sample.ecommerce.batch.job;

import com.sample.ecommerce.batch.mapper.CategoryMapper;
import com.sample.ecommerce.batch.writer.CategoryWriter;
import com.sample.ecommerce.domain.Category;
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
public class CategoryJob {
    
    @Autowired
    private CategoryWriter categoryWriter;   

    // tag::jobstep[]
    @Bean
    public Job importCategorysJob(JobBuilderFactory jobs,StepBuilderFactory stepBuilderFactory) {
        
        FlatFileItemReader<Category> categoryreader = new FlatFileItemReader<>();
        categoryreader.setResource(new ClassPathResource("data/categories.csv"));
        categoryreader.setLineMapper(new DefaultLineMapper<Category>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setNames(new String[] { "id", "label","parent" });
            }});
            setFieldSetMapper(new CategoryMapper());
        }});
        
        Step step1 = stepBuilderFactory.get("step1")
                .<Category, Category> chunk(10)
                .reader(categoryreader)                
                .writer(categoryWriter)
                .build();
        
        return jobs.get("importCategorysJob")
                .incrementer(new RunIdIncrementer())
                .flow(step1)
                .end()
                .build();
    }

  
}
