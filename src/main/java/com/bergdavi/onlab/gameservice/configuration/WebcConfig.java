package com.bergdavi.onlab.gameservice.configuration;

import com.bergdavi.onlab.gameservice.converter.GameFromJpaConverter;
import com.bergdavi.onlab.gameservice.converter.GameplayFromJpaConverter;
import com.bergdavi.onlab.gameservice.converter.UserDetailsFromJpaConverter;
import com.bergdavi.onlab.gameservice.converter.UserFromJpaConverter;
import com.bergdavi.onlab.gameservice.converter.UserGameplayFromJpaConverter;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebcConfig
 */
@Configuration
public class WebcConfig implements WebMvcConfigurer{
    
    @Override
    public void addFormatters(FormatterRegistry registry) {
        ConversionService conversionService = (ConversionService) registry;

        registry.addConverter(new GameFromJpaConverter());
        registry.addConverter(new GameplayFromJpaConverter(conversionService));
        registry.addConverter(new UserFromJpaConverter());
        registry.addConverter(new UserGameplayFromJpaConverter(conversionService));
        registry.addConverter(new UserDetailsFromJpaConverter(conversionService));
    }
}