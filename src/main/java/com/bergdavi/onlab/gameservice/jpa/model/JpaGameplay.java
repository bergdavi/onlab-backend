package com.bergdavi.onlab.gameservice.jpa.model;

import java.util.Date;
import java.util.SortedSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import com.bergdavi.onlab.gameservice.model.Status;

import org.hibernate.annotations.GenericGenerator;

/**
 * JpaGameplay
 */
@Entity(name = "gameplays")
public class JpaGameplay implements Comparable<JpaGameplay> {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id")
    private String id;

    @Column(name = "next_user_idx")
    private Integer nextUserIdx = 0;

    @Column(name = "user_count")
    private Integer userCount;

    @Column(name = "game_state")
    private String gameState;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status = Status.IN_PROGRESS;

    @Column(name = "started")
    private Date started;

    @Column(name = "finished")
    private Date finished;

    @ManyToOne
    @JoinColumn(name="game_id", referencedColumnName = "id", nullable = false)
    private JpaGame game;

    @OneToMany(mappedBy = "gameplay", fetch = FetchType.EAGER)
    @OrderBy("")
    private SortedSet<JpaUserGameplay> userGameplays;

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


    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }


    public Date getStarted() {
        return this.started;
    }

    public void setStarted(Date started) {
        this.started = started;
    }

    public Date getFinished() {
        return this.finished;
    }

    public void setFinished(Date finished) {
        this.finished = finished;
    }


    public SortedSet<JpaUserGameplay> getUserGameplays() {
        return this.userGameplays;
    }

    public void setUserGameplays(SortedSet<JpaUserGameplay> userGameplays) {
        this.userGameplays = userGameplays;
    }

    @Override
    public int compareTo(JpaGameplay o) {
        // TODO Auto-generated method stub
        return id.compareTo(o.id);
    }
}
