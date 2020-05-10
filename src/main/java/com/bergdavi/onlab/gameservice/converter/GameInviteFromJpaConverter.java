package com.bergdavi.onlab.gameservice.converter;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.bergdavi.onlab.gameservice.jpa.model.JpaGameInvite;
import com.bergdavi.onlab.gameservice.model.Game;
import com.bergdavi.onlab.gameservice.model.GameInvite;
import com.bergdavi.onlab.gameservice.model.User;
import com.bergdavi.onlab.gameservice.model.UserGameInvite;

import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;

public class GameInviteFromJpaConverter implements Converter<JpaGameInvite, GameInvite> {

    private ConversionService conversionService;

    public GameInviteFromJpaConverter(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public GameInvite convert(JpaGameInvite source) {
        return new GameInvite(
            source.getInviteId(),
            conversionService.convert(source.getGame(), Game.class),
            conversionService.convert(source.getInviter(), User.class),
            StreamSupport.stream(source.getInvitees().spliterator(), false).map(ugp -> {
                    UserGameInvite userGameInvite = new UserGameInvite(
                        conversionService.convert(ugp.getInvited(), User.class),
                        ugp.getAccepted()
                    );
                    return userGameInvite;
                }).collect(Collectors.toList()),
            source.getInviteDate().toString()
        );
    }
}
