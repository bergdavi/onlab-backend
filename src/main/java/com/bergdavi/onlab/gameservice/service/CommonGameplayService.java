package com.bergdavi.onlab.gameservice.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Service;

/**
 * GameplayService
 */
@Service
public class CommonGameplayService {

    private static Map<String, Class<?>> delegateServices = new HashMap<>();

    static {
        final ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(true);

        scanner.addIncludeFilter(new AnnotationTypeFilter(GameService.class));

        for (BeanDefinition bd : scanner.findCandidateComponents(""))
            System.out.println(bd.getBeanClassName());
    }
    
}