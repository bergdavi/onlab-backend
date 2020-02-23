package com.bergdavi.onlab.gameservice.jpa.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * JpaUser
 */
@Entity(name = "user_details")
@Table(name = "user_details")
public class JpaUser {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", columnDefinition = "VARCHAR(36)")
    private String id;
    
    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "email", unique = true)
    private String email;

    @OneToMany(mappedBy = "user")
    private Set<JpaUserGameplay> gameplays;

    public JpaUser() {}
    

    public JpaUser(String username, String email) {
        this.username = username;
        this.email = email;
    }


    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<JpaUserGameplay> getGameplays() {
        return this.gameplays;
    }

    public void setGameplays(Set<JpaUserGameplay> gameplays) {
        this.gameplays = gameplays;
    }
}
