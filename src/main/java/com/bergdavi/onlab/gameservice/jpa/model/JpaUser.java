package com.bergdavi.onlab.gameservice.jpa.model;

import java.io.Serializable;
import java.util.Set;
import java.util.SortedSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SortNatural;

/**
 * JpaUser
 */
@Entity(name = "user_details")
@Table(name = "user_details")
public class JpaUser implements Comparable<JpaUser>, Serializable {
    private static final long serialVersionUID = 4518075640376754531L;

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", columnDefinition = "VARCHAR(36)")
    private String id;
    
    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "banned")
    private Boolean banned = false;

    @OneToMany(mappedBy = "user")
    @SortNatural
    private SortedSet<JpaUserGameplay> gameplays;

    @OneToMany(mappedBy = "invited")
    private Set<JpaGameInvited> invites;

    @OneToMany(mappedBy = "inviter")
    @SortNatural
    private SortedSet<JpaGameInvite> sentInvites;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<JpaGameQueue> queue;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private Set<JpaAuthority> authorities;

    public JpaUser() {}
    
    public JpaUser(String userId) {
        this.id = userId;
    }

    public JpaUser(String username, String email) {
        this.username = username;
        this.email = email;
    }


    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getBanned() {
        return banned;
    }

    public void setBanned(Boolean banned) {
        this.banned = banned;
    }

    public SortedSet<JpaUserGameplay> getGameplays() {
        return this.gameplays;
    }

    public void setGameplays(SortedSet<JpaUserGameplay> gameplays) {
        this.gameplays = gameplays;
    }

    public Set<JpaGameInvited> getInvites() {
        return invites;
    }

    public void setInvites(Set<JpaGameInvited> invites) {
        this.invites = invites;
    }

    public SortedSet<JpaGameInvite> getSentInvites() {
        return sentInvites;
    }

    public void setSentInvites(SortedSet<JpaGameInvite> sentInvites) {
        this.sentInvites = sentInvites;
    }

    public Set<JpaAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<JpaAuthority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public int compareTo(JpaUser o) {
        return username.compareTo(o.username);
    }
}
