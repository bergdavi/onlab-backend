package com.bergdavi.onlab.gameservice.converter;

import com.bergdavi.onlab.gameservice.jpa.model.JpaUserGameplay;
import com.bergdavi.onlab.gameservice.model.Gameplay;
import com.bergdavi.onlab.gameservice.model.UserGameplay;

import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;

/**
 * UserGameplayConverter
 */
public class UserGameplayFromJpaConverter implements Converter<JpaUserGameplay, UserGameplay> {

    private ConversionService conversionService;

    public UserGameplayFromJpaConverter(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public UserGameplay convert(JpaUserGameplay jpaUserGameplay) {
        return new UserGameplay(
            jpaUserGameplay.getUserIdx().longValue(),
            conversionService.convert(jpaUserGameplay.getGameplay(), Gameplay.class)
        );
    }
}
