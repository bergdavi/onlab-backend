package com.bergdavi.onlab.gameservice.service;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface GameService {

    String id();

    String name();

    String description();

    int minPlayers() default 2;

    int maxPlayers() default 2;

    Class<?> gameStateType();
    
    Class<?> gameTurnType();
}