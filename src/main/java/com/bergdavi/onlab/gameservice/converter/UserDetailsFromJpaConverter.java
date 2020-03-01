package com.bergdavi.onlab.gameservice.converter;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.bergdavi.onlab.gameservice.jpa.model.JpaUser;
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
        return new UserDetails(
            conversionService.convert(jpaUser, User.class),
            StreamSupport.stream(jpaUser.getGameplays().spliterator(), false)
                .map(ugp -> conversionService.convert(ugp, UserGameplay.class)).collect(Collectors.toList())
        );
    }
}
