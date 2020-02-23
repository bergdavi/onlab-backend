package com.bergdavi.onlab.gameservice.jpa.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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

    public JpaGameQueue() {
    }

    public JpaGameQueuePk getQueueId() {
        return this.queueId;
    }

    public void setQueueId(JpaGameQueuePk queueId) {
        this.queueId = queueId;
    }

    public String getUser() {
        return queueId.getUser();
    }

    public void setUser(String user) {
        queueId.setUser(user);
    }

    public String getGame() {
        return queueId.getGame();
    }

    public void setGame(String game) {
        queueId.setGame(game);
    }

    public Date getJoined() {
        return this.joined;
    }

    public void setJoined(Date joined) {
        this.joined = joined;
    }
}