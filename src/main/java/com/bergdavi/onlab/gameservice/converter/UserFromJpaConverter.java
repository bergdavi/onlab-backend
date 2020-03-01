package com.bergdavi.onlab.gameservice.converter;

import com.bergdavi.onlab.gameservice.jpa.model.JpaUser;
import com.bergdavi.onlab.gameservice.model.Type;
import com.bergdavi.onlab.gameservice.model.User;

import org.springframework.core.convert.converter.Converter;

/**
 * UserFromJpaConverter
 */
public class UserFromJpaConverter implements Converter<JpaUser, User> {

    @Override
    public User convert(JpaUser jpaUser) {
        // Password is empty string to not leak data
        // TODO fix user type (currently always USER type)
        return new User(jpaUser.getId(), jpaUser.getUsername(), jpaUser.getEmail(), "", Type.USER);
    }
}