package com.moldavets.tiktok_telegram_bot.model.Impl;

import com.moldavets.tiktok_telegram_bot.model.AbstractAuditingEntity;
import com.moldavets.tiktok_telegram_bot.model.ConditionStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "telegram_users")
public class TelegramUser extends AbstractAuditingEntity<Long> {

    @Id
    private Long id;

    private String username;

    private ConditionStatus status;

    public TelegramUser() {
    }

    public TelegramUser(Long id, String username, ConditionStatus status) {
        this.id = id;
        this.username = username;
        this.status = status;
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

    public ConditionStatus getStatus() {
        return status;
    }

    public void setStatus(ConditionStatus status) {
        this.status = status;
    }
}
