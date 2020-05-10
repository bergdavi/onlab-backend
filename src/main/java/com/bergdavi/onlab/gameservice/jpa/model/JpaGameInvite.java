package com.bergdavi.onlab.gameservice.jpa.model;

import java.util.Date;
import java.util.HashSet;
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

@Entity(name = "game_invites")
@Table(name = "game_invites")
public class JpaGameInvite implements Comparable<JpaGameInvite> {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "invite_id")
    private String inviteId;

    @Column(name = "invite_date")
    private Date inviteDate;

    @OneToMany(mappedBy = "invite")
    private Set<JpaGameInvited> invitees = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "inviter")
    private JpaUser inviter;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private JpaGame game;

    public JpaGameInvite() {
    }


    public String getInviteId() {
        return this.inviteId;
    }

    public void setInviteId(String inviteId) {
        this.inviteId = inviteId;
    }

    public Date getInviteDate() {
        return this.inviteDate;
    }

    public void setInviteDate(Date inviteDate) {
        this.inviteDate = inviteDate;
    }

    public Set<JpaGameInvited> getInvitees() {
        return this.invitees;
    }

    public void setInvitees(Set<JpaGameInvited> invitees) {
        this.invitees = invitees;
    }

    public JpaUser getInviter() {
        return this.inviter;
    }

    public void setInviter(JpaUser inviter) {
        this.inviter = inviter;
    }

    public JpaGame getGame() {
        return this.game;
    }

    public void setGame(JpaGame game) {
        this.game = game;
    }
   
    public void addInvitee(String inviteeId) {
        JpaGameInvited jpaGameInvited = new JpaGameInvited();
        JpaGameInvitedPK invitedId = new JpaGameInvitedPK();
        invitedId.setInviteId(inviteId);
        invitedId.setUserId(inviteeId);
        jpaGameInvited.setInvitedId(invitedId);
        invitees.add(jpaGameInvited);
    }

    @Override
    public int compareTo(JpaGameInvite o) {
        return inviteDate.compareTo(o.inviteDate);
    }
}
