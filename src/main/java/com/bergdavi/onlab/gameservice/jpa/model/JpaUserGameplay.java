package com.bergdavi.onlab.gameservice.jpa.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * JpaGameplay
 */
@Entity(name = "user_gameplays")
@Table(name = "user_gameplays")
public class JpaUserGameplay {

    @EmbeddedId
    private JpaUserGameplayPk id;

    @Column(name = "user_idx")
    private Integer userIdx;

    @ManyToOne
    @JoinColumn(name = "gameplay", insertable = false, updatable = false)
    private JpaGameplay gameplay;

    @ManyToOne
    @JoinColumn(name = "user", insertable = false, updatable = false)
    private JpaUser user;

    public JpaUserGameplay() {}

    public JpaUserGameplay(String user, String gameplay, int userIdx) {
        this.id = new JpaUserGameplayPk(user, gameplay);
        this.userIdx = userIdx;
    }

    public String getUserId() {
        return id.getUser();
    }

    public void setUserId(String user) {
        id.setUser(user);
    }

    public Integer getUserIdx() {
        return this.userIdx;
    }

    public void setUserIdx(Integer userIdx) {
        this.userIdx = userIdx;
    }

    public String getGameplayId() {
        return id.getGameplay();
    }

    public void setGameplayId(String gameplay) {
        id.setGameplay(gameplay);
    }


    public JpaUserGameplayPk getId() {
        return this.id;
    }

    public void setId(JpaUserGameplayPk id) {
        this.id = id;
    }

    public JpaGameplay getGameplay() {
        return this.gameplay;
    }

    public void setGameplay(JpaGameplay gameplay) {
        this.gameplay = gameplay;
    }

    public JpaUser getUser() {
        return this.user;
    }

    public void setUser(JpaUser user) {
        this.user = user;
    }

}
