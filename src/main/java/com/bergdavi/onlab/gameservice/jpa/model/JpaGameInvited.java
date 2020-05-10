package com.bergdavi.onlab.gameservice.jpa.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity(name = "game_invited")
@Table(name = "game_invited")
public class JpaGameInvited {

    @EmbeddedId
    private JpaGameInvitedPK invitedId;

    @Column(name = "accepted")
    private Boolean accepted = false;

    @ManyToOne
    @JoinColumn(name = "invite_id", insertable = false, updatable = false)
    private JpaGameInvite invite;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private JpaUser invited;

    public JpaGameInvited() {
    }

    public JpaGameInvited(JpaGameInvitedPK invitedId, Boolean accepted, JpaGameInvite invite) {
        this.invitedId = invitedId;
        this.accepted = accepted;
        this.invite = invite;
    }

    public JpaGameInvitedPK getInvitedId() {
        return this.invitedId;
    }

    public void setInvitedId(JpaGameInvitedPK invitedId) {
        this.invitedId = invitedId;
    }

    public Boolean isAccepted() {
        return this.accepted;
    }

    public Boolean getAccepted() {
        return this.accepted;
    }

    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }

    public JpaGameInvite getInvite() {
        return this.invite;
    }

    public void setInvite(JpaGameInvite invite) {
        this.invite = invite;
    }

    public JpaUser getInvited() {
        return invited;
    }

    public void setInvited(JpaUser invited) {
        this.invited = invited;
    }
}
