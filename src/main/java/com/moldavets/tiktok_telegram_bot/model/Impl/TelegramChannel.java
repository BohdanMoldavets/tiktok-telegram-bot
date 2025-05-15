package com.moldavets.tiktok_telegram_bot.model.Impl;

import com.moldavets.tiktok_telegram_bot.model.AbstractAuditingEntity;
import com.moldavets.tiktok_telegram_bot.model.ConditionStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "telegram_channels")
public class TelegramChannel extends AbstractAuditingEntity<Long> {
    @Id
    private Long id;

    private String channelLink;

    private ConditionStatus status;

    public TelegramChannel() {
    }

    public TelegramChannel(Long id, String channelLink, ConditionStatus status) {
        this.id = id;
        this.channelLink = channelLink;
        this.status = status;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChannelLink() {
        return channelLink;
    }

    public void setChannelLink(String channelLink) {
        this.channelLink = channelLink;
    }

    public ConditionStatus getStatus() {
        return status;
    }

    public void setStatus(ConditionStatus status) {
        this.status = status;
    }
}
