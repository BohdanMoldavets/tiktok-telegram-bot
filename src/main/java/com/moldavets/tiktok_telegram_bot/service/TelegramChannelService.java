package com.moldavets.tiktok_telegram_bot.service;

import com.moldavets.tiktok_telegram_bot.model.Impl.TelegramChannel;
import com.moldavets.tiktok_telegram_bot.model.TelegramChannelStatus;

import java.util.List;

public interface TelegramChannelService {

    TelegramChannel getById(Long id);
    List<TelegramChannel> getAllWhereStatusIsActive();
    void save(TelegramChannel telegramChannel);
    void saveOrUpdate(TelegramChannel telegramChannel);
    void updateStatusById(Long id, TelegramChannelStatus status);

}
