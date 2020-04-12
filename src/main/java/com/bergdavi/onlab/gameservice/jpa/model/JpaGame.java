package com.bergdavi.onlab.gameservice.jpa.model;

import java.util.Set;
import java.util.SortedSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

/**
 * JpaGame
 */
@Entity(name = "games")
@Table(name = "games")
public class JpaGame {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "min_players")
    private Integer minPlayers;

    @Column(name = "max_players")
    private Integer maxPlayers;

    @Column(name = "initial_state")
    private String initialState;

    @OneToMany(mappedBy = "game")
    @OrderBy("started")
    private SortedSet<JpaGameplay> gameplays;

    @OneToMany(mappedBy = "game")
    private Set<JpaGameInvite> gameInvites;

    public JpaGame() {}

    public JpaGame(String gameId) {
        this.id = gameId;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getMinPlayers() {
        return this.minPlayers;
    }

    public void setMinPlayers(Integer minPlayers) {
        this.minPlayers = minPlayers;
    }

    public Integer getMaxPlayers() {
        return this.maxPlayers;
    }

    public void setMaxPlayers(Integer maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public String getInitialState() {
        return this.initialState;
    }

    public void setInitialState(String initialState) {
        this.initialState = initialState;
    }

    public SortedSet<JpaGameplay> getGameplays() {
        return this.gameplays;
    }

    public void setGameplays(SortedSet<JpaGameplay> gameplays) {
        this.gameplays = gameplays;
    }
}
