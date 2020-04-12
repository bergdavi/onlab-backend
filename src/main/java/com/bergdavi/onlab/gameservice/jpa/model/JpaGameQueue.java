package com.bergdavi.onlab.gameservice.jpa.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * JpaGameQueue
 */
@Entity(name = "game_queue")
@Table(name = "game_queue")
public class JpaGameQueue {

    @EmbeddedId
    private JpaGameQueuePk queueId;

    @Column(name = "joined")
    private Date joined;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private JpaUser user;

    public JpaGameQueue() {
    }

    public JpaGameQueuePk getQueueId() {
        return this.queueId;
    }

    public void setQueueId(JpaGameQueuePk queueId) {
        this.queueId = queueId;
    }

    public String getUserId() {
        return queueId.getUser();
    }

    public void setUserId(String user) {
        queueId.setUser(user);
    }

    public String getGameId() {
        return queueId.getGame();
    }

    public void setGameId(String game) {
        queueId.setGame(game);
    }

    public Date getJoined() {
        return this.joined;
    }

    public void setJoined(Date joined) {
        this.joined = joined;
    }
    
    public JpaUser getUser() {
        return user;
    }

    public void setUser(JpaUser user) {
        this.user = user;
    }
}