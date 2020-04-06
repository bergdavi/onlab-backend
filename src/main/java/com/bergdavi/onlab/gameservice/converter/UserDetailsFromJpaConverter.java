package com.bergdavi.onlab.gameservice.converter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.bergdavi.onlab.gameservice.jpa.model.JpaUser;
import com.bergdavi.onlab.gameservice.jpa.model.JpaUserGameplay;
import com.bergdavi.onlab.gameservice.model.User;
import com.bergdavi.onlab.gameservice.model.UserDetails;
import com.bergdavi.onlab.gameservice.model.UserGameplay;

import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * UserFromJpaConverter
 */
@Component
public class UserDetailsFromJpaConverter implements Converter<JpaUser, UserDetails> {

    private ConversionService conversionService;

    public UserDetailsFromJpaConverter(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public UserDetails convert(JpaUser jpaUser) {
        ArrayList<JpaUserGameplay> gameplays = new ArrayList<>(jpaUser.getGameplays());
        gameplays.sort((JpaUserGameplay g1, JpaUserGameplay g2) -> {
            Date d1 = g1.getGameplay().getFinished();
            Date d2 = g2.getGameplay().getFinished();
            if(d1 == null && d2 == null) {
                return g1.getGameplay().getStarted().compareTo(g2.getGameplay().getStarted());
            } else if(d1 == null) {
                return 1;
            } else if(d2 == null) {
                return -1;
            }
            return d1.compareTo(d2);
        });
        Collections.reverse(gameplays);
        return new UserDetails(
            conversionService.convert(jpaUser, User.class),
            StreamSupport.stream(gameplays.spliterator(), false)
                .map(ugp -> conversionService.convert(ugp, UserGameplay.class)).collect(Collectors.toList())
        );
    }
}
