package com.moldavets.tiktok_telegram_bot.model.Impl;

import com.moldavets.tiktok_telegram_bot.model.AbstractAuditingEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "telegram_user")
public class TelegramUser extends AbstractAuditingEntity<Long> {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "status")
    private String status;

    @Column(name = "is_subscribed")
    private Boolean isSubscribed;

    public TelegramUser() {
    }

    public TelegramUser(Long id, String username, String status, Boolean isSubscribed) {
        this.id = id;
        this.username = username;
        this.status = status;
        this.isSubscribed = isSubscribed;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean isSubscribed() {
        return isSubscribed;
    }

    public void setSubscribed(Boolean subscribed) {
        isSubscribed = subscribed;
    }
}
