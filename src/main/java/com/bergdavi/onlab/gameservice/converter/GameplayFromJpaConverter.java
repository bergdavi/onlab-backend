package com.bergdavi.onlab.gameservice.converter;

import java.util.ArrayList;
import java.util.List;

import com.bergdavi.onlab.gameservice.jpa.model.JpaGameplay;
import com.bergdavi.onlab.gameservice.jpa.model.JpaUserGameplay;
import com.bergdavi.onlab.gameservice.model.Game;
import com.bergdavi.onlab.gameservice.model.Gameplay;
import com.bergdavi.onlab.gameservice.model.User;

import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;

/**
 * GameplayFromJpaConverter
 */
public class GameplayFromJpaConverter implements Converter<JpaGameplay, Gameplay> {

    private ConversionService conversionService;

    public GameplayFromJpaConverter(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public Gameplay convert(JpaGameplay jpaGameplay) {
        List<User> users = new ArrayList<>();
        User nextTurn = null;
        for(JpaUserGameplay jpaUserGameplay : jpaGameplay.getUserGameplays()) {
            User user = conversionService.convert(jpaUserGameplay.getUser(), User.class);
            users.add(user);
            if(jpaUserGameplay.getUserIdx().equals(jpaGameplay.getNextUserIdx())) {
                nextTurn = user;
            }
        }

        return new Gameplay(
            jpaGameplay.getId(),
            conversionService.convert(jpaGameplay.getGame(), Game.class),
            users,
            nextTurn,
            jpaGameplay.getGameState()
        );
    }
}
