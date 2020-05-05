package com.bergdavi.onlab.gameservice.converter;

import com.bergdavi.onlab.gameservice.jpa.model.JpaGame;
import com.bergdavi.onlab.gameservice.model.Game;

import org.springframework.core.convert.converter.Converter;

/**
 * GameFromJpaConverter
 */
public class GameFromJpaConverter implements Converter<JpaGame, Game> {

    @Override
    public Game convert(JpaGame jpaGame) {
        Game game = new Game(
            jpaGame.getId(),
            jpaGame.getName(),
            jpaGame.getDescription(),
            jpaGame.getMinPlayers().longValue(),
            jpaGame.getMaxPlayers().longValue(),
            // TODO fix ratings
            0.0,
            0L,
            jpaGame.getEnabled()
        );
        return game;
    }

    
}