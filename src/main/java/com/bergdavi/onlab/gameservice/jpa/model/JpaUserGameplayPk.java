package com.bergdavi.onlab.gameservice.jpa.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * JpaUserGameplayPk
 */
@Embeddable
public class JpaUserGameplayPk implements Serializable {

    private static final long serialVersionUID = 5957391081245194259L;

    @Column(name = "user")
    private String user;

    @Column(name = "gameplay")
    private String gameplay;    

    public JpaUserGameplayPk() {}

    public JpaUserGameplayPk(String user, String gameplay) {
        this.user = user;
        this.gameplay = gameplay;
    }

    public String getUser() {
        return this.user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getGameplay() {
        return this.gameplay;
    }

    public void setGameplay(String gameplay) {
        this.gameplay = gameplay;
    }


    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof JpaUserGameplayPk)) {
            return false;
        }
        JpaUserGameplayPk jpaUserGameplayPk = (JpaUserGameplayPk) o;
        return Objects.equals(user, jpaUserGameplayPk.user) && Objects.equals(gameplay, jpaUserGameplayPk.gameplay);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, gameplay);
    }

}