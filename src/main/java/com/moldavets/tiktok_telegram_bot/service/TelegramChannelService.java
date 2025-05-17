package com.moldavets.tiktok_telegram_bot.service;

import com.moldavets.tiktok_telegram_bot.model.Impl.TelegramChannel;

import java.util.List;

public interface TelegramChannelService {

    TelegramChannel getById(Long id);
    List<TelegramChannel> getAllWhereStatusIsActive();

}
