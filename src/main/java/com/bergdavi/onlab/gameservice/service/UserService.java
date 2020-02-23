package com.bergdavi.onlab.gameservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.bergdavi.onlab.gameservice.jpa.model.JpaGame;
import com.bergdavi.onlab.gameservice.jpa.model.JpaGameplay;
import com.bergdavi.onlab.gameservice.jpa.model.JpaUser;
import com.bergdavi.onlab.gameservice.jpa.model.JpaUserGameplay;
import com.bergdavi.onlab.gameservice.jpa.repository.UserRepository;
import com.bergdavi.onlab.gameservice.model.Game;
import com.bergdavi.onlab.gameservice.model.Gameplay;
import com.bergdavi.onlab.gameservice.model.Type;
import com.bergdavi.onlab.gameservice.model.User;
import com.bergdavi.onlab.gameservice.model.UserDetails;
import com.bergdavi.onlab.gameservice.model.UserGameplay;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Service;

/**
 * UserService
 */
@Service
public class UserService {

    @Autowired
    private JdbcUserDetailsManager jdbcUserDetailsManager;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    public User registerUser(User user) {
        List<String> roles = new ArrayList<>();
        roles.add("USER");
        if (user.getType() == Type.ADMIN) {
            roles.add("ADMIN");
        }
        if (jdbcUserDetailsManager.userExists(user.getUsername())) {
            // TODO replace with more appropriate exception
            // Return HTTP 409
            throw new RuntimeException();
        }       
        JpaUser jpaUser = new JpaUser(user.getUsername(), user.getEmail());
        userRepository.save(jpaUser);
        user.setId(jpaUser.getId());
        jdbcUserDetailsManager.createUser(org.springframework.security.core.userdetails.User
            .withUsername(user.getUsername()).password(passwordEncoder.encode(user.getPassword()))
            .roles(roles.toArray(new String[roles.size()])).build());
        return user;
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        for (JpaUser user : userRepository.findAll()) {
            // TODO fix user type (Type.USER)
            users.add(new User(user.getId(), user.getUsername(), user.getEmail(), "", Type.USER));
        }
        return users;
    }

    public UserDetails getUserById(String id) {
        Optional<JpaUser> optUser = userRepository.findById(id);
        if(optUser.isPresent()) {
            JpaUser jpaUser = optUser.get();
            
            // TODO this should be moved to the gameplay service
            List<UserGameplay> gameplays = new ArrayList<>();
            for(JpaUserGameplay jpaUserGameplay : jpaUser.getGameplays()) {
                JpaGameplay jpaGameplay = jpaUserGameplay.getGameplay();
                JpaGame jpaGame = jpaGameplay.getGame();
                // TODO use spring converters instead of manual conversion: https://www.baeldung.com/spring-type-conversions
                List<User> users = new ArrayList<>();
                User nextTurnUser = null;
                for(JpaUserGameplay jpaUserGameplayInGame : jpaGameplay.getUserGameplays()){
                    JpaUser jpaUserInGame = jpaUserGameplayInGame.getUser();
                    // TODO fix user type (Type.USER)
                    User user = new User(jpaUserInGame.getId(), jpaUserInGame.getUsername(), jpaUserInGame.getEmail(), "", Type.USER);
                    if(jpaUserGameplay.getUserIdx().equals(jpaGameplay.getNextUserIdx())) {
                        nextTurnUser = user;
                    }
                    users.add(user);
                }
                Game game = new Game(jpaGame.getId(), jpaGame.getName(), jpaGame.getDescription(), jpaGame.getMinPlayers().longValue(), jpaGame.getMaxPlayers().longValue(), jpaGame.getThumbnail(), 0.0, 0L);                
                Gameplay gameplay = new Gameplay(jpaGameplay.getId(), game, users, nextTurnUser, jpaGameplay.getGameState());
                UserGameplay userGameplay = new UserGameplay(jpaUserGameplay.getUserIdx().longValue(), gameplay);
                gameplays.add(userGameplay);
            }
            // TODO fix user type (Type.USER)
            return new UserDetails(new User(jpaUser.getId(), jpaUser.getUsername(), jpaUser.getEmail(), "", Type.USER), gameplays);
        } else {
            // TODO throw exception instead
            return null;
        }
    }

    public UserDetails getUserByUsername(String username) {
        return getUserById(getUserIdByUsername(username));
    }

    public String getUserIdByUsername(String username) {
        return userRepository.getIdByUsername(username);
    }

}