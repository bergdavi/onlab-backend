package com.bergdavi.onlab.gameservice.converter;

import com.bergdavi.onlab.gameservice.jpa.model.JpaAuthority;
import com.bergdavi.onlab.gameservice.jpa.model.JpaUser;
import com.bergdavi.onlab.gameservice.model.User;
import com.bergdavi.onlab.gameservice.model.UserType;

import org.springframework.core.convert.converter.Converter;

/**
 * UserFromJpaConverter
 */
public class UserFromJpaConverter implements Converter<JpaUser, User> {

    @Override
    public User convert(JpaUser jpaUser) {
        UserType userType = UserType.USER;
        for(JpaAuthority authority : jpaUser.getAuthorities()) {
            if(authority.getAuthority().equalsIgnoreCase("ROLE_ADMIN")) {
                userType = UserType.ADMIN;
                break;
            }
        }
        // Password is empty string to not leak data
        return new User(jpaUser.getId(), jpaUser.getUsername(), jpaUser.getEmail(), "", userType, jpaUser.getBanned());
    }
}