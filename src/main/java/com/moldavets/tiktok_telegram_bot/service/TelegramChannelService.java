package com.moldavets.tiktok_telegram_bot.service;

import com.moldavets.tiktok_telegram_bot.model.Impl.TelegramChannel;

public interface TelegramChannelService {

    TelegramChannel getById(Long id);

}
