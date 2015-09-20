/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sample.ecommerce.batch.job;

import com.sample.ecommerce.batch.mapper.TermMapper;
import com.sample.ecommerce.batch.writer.TermWriter;
import com.sample.ecommerce.domain.Term;
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
public class TermJob {
    
    @Autowired
    private TermWriter productWriter;    

    // tag::jobstep[]
    @Bean
    public Job importTermsJob(JobBuilderFactory jobs,StepBuilderFactory stepBuilderFactory) {
        
        FlatFileItemReader<Term> termreader = new FlatFileItemReader<>();
        termreader.setResource(new ClassPathResource("data/terms.csv"));
        termreader.setLineMapper(new DefaultLineMapper<Term>() {{
            setLineTokenizer(new DelimitedLineTokenizer());
            setFieldSetMapper(new TermMapper());
        }});
        
        Step step1 = stepBuilderFactory.get("step1")
                .<Term, Term> chunk(10)
                .reader(termreader)                
                .writer(productWriter)
                .build();
        
        return jobs.get("importTermsJob")
                .incrementer(new RunIdIncrementer())
                .flow(step1)
                .end()
                .build();
    }

  
}
