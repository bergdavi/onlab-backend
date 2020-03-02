package com.bergdavi.onlab.gameservice.jpa.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * JpaGameQueuePk
 */
@Embeddable
public class JpaGameQueuePk implements Serializable {

    private static final long serialVersionUID = -1602930330105848129L;

    @Column(name = "user_id")
    private String user;

    @Column(name = "game_id")
    private String game;

    public JpaGameQueuePk() {
    }

    public JpaGameQueuePk(String user, String game) {
        this.user = user;
        this.game = game;
    }

    public String getUser() {
        return this.user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getGame() {
        return this.game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof JpaGameQueuePk)) {
            return false;
        }
        JpaGameQueuePk jpaGameQueuePk = (JpaGameQueuePk) o;
        return Objects.equals(user, jpaGameQueuePk.user) && Objects.equals(game, jpaGameQueuePk.game);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, game);
    }    
}