/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sample.ecommerce;

import java.io.File;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.thymeleaf.templateresolver.FileTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolver;

@Configuration
@EnableConfigurationProperties(ThymeleafProperties.class)
public class TemplateConfiguration {
    
    private static final Logger LOGGER = getLogger(TemplateConfiguration.class);

    @Autowired
    private ThymeleafProperties properties;

    @Bean
    public ITemplateResolver defaultTemplateResolver() {
        FileTemplateResolver resolver = new FileTemplateResolver();
        File file = new File("src/main/resources/static");
        resolver.setPrefix(file.getAbsolutePath() + File.separator);
        intializeResolver(resolver);     
        return resolver;
    }

    private void intializeResolver(TemplateResolver resolver) {
        resolver.setSuffix(this.properties.getSuffix());
        resolver.setTemplateMode(this.properties.getMode());
        resolver.setCharacterEncoding(this.properties.getEncoding());
        resolver.setCacheable(this.properties.isCache());
    }
}