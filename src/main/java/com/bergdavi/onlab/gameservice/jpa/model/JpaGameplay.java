package com.bergdavi.onlab.gameservice.jpa.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * JpaGameplay
 */
@Entity(name = "gameplays")
@Table(name = "gameplays")
public class JpaGameplay {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id")
    private String id;

    @Column(name = "next_user_idx")
    private Integer nextUserIdx = 1;

    @Column(name = "user_count")
    private Integer userCount;

    @Column(name = "game_state")
    private String gameState;

    @ManyToOne
    @JoinColumn(name="game", referencedColumnName = "id", nullable = false)
    private JpaGame game;

    @OneToMany(mappedBy = "gameplay")
    private Set<JpaUserGameplay> userGameplays;

    public JpaGameplay() {}


    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getNextUserIdx() {
        return this.nextUserIdx;
    }

    public void setNextUserIdx(Integer nextUserIdx) {
        this.nextUserIdx = nextUserIdx;
    }

    public Integer getUserCount() {
        return this.userCount;
    }

    public void setUserCount(Integer userCount) {
        this.userCount = userCount;
    }

    public String getGameState() {
        return this.gameState;
    }

    public void setGameState(String gameState) {
        this.gameState = gameState;
    }


    public JpaGame getGame() {
        return this.game;
    }

    public void setGame(JpaGame game) {
        this.game = game;
    }


    public Set<JpaUserGameplay> getUserGameplays() {
        return this.userGameplays;
    }

    public void setUserGameplays(Set<JpaUserGameplay> userGameplays) {
        this.userGameplays = userGameplays;
    }


}
