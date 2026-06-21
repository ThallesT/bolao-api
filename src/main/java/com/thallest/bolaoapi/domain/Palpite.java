package com.thallest.bolaoapi.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
    name = "guesses",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"match_id", "user_id"})
    }
)
public class Palpite {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "group_id", nullable = false)
    private Grupo group;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "match_id", nullable = false)
    private Partida match;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(nullable = false)
    private Integer homeScoreGuess;

    @Column(nullable = false)
    private Integer awayScoreGuess;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    @PrePersist
    void onCreate() {
        Instant now = Instant.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Grupo getGroup() {
        return group;
    }

    public void setGroup(Grupo group) {
        this.group = group;
    }

    public Partida getMatch() {
        return match;
    }

    public void setMatch(Partida match) {
        this.match = match;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public Integer getHomeScoreGuess() {
        return homeScoreGuess;
    }

    public void setHomeScoreGuess(Integer homeScoreGuess) {
        this.homeScoreGuess = homeScoreGuess;
    }

    public Integer getAwayScoreGuess() {
        return awayScoreGuess;
    }

    public void setAwayScoreGuess(Integer awayScoreGuess) {
        this.awayScoreGuess = awayScoreGuess;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}

