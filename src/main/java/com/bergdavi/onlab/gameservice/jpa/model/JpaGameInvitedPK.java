package com.bergdavi.onlab.gameservice.jpa.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;

/**
 * JpaGameInvitePK
 */
public class JpaGameInvitedPK implements Serializable {

    private static final long serialVersionUID = -6904857033797912905L;

    @Column(name = "invite_id")
    private String inviteId;

    @Column(name = "user_id")
    private String userId;


    public JpaGameInvitedPK() {
    }

    public JpaGameInvitedPK(String inviteId, String userId) {
        this.inviteId = inviteId;
        this.userId = userId;
    }

    public String getInviteId() {
        return this.inviteId;
    }

    public void setInviteId(String inviteId) {
        this.inviteId = inviteId;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public JpaGameInvitedPK invite_id(String invite_id) {
        this.inviteId = invite_id;
        return this;
    }

    public JpaGameInvitedPK user_id(String user_id) {
        this.userId = user_id;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof JpaGameInvitedPK)) {
            return false;
        }
        JpaGameInvitedPK jpaGameInvitedPK = (JpaGameInvitedPK) o;
        return Objects.equals(inviteId, jpaGameInvitedPK.inviteId) && Objects.equals(userId, jpaGameInvitedPK.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(inviteId, userId);
    }    
}