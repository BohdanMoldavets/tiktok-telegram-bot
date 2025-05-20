package com.moldavets.tiktok_telegram_bot.model.Impl;

import com.moldavets.tiktok_telegram_bot.model.AbstractAuditingEntity;
import com.moldavets.tiktok_telegram_bot.model.TelegramChannelStatus;
import jakarta.persistence.*;

@Entity
@Table(name = "telegram_channels")
public class TelegramChannel extends AbstractAuditingEntity<Long> {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "channel_link")
    private String channelLink;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private TelegramChannelStatus status;

    public TelegramChannel() {
    }

    public TelegramChannel(Long id, String channelLink, TelegramChannelStatus status) {
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

    public TelegramChannelStatus getStatus() {
        return status;
    }

    public void setStatus(TelegramChannelStatus status) {
        this.status = status;
    }
}
