package com.moldavets.tiktok_telegram_bot.service;

import com.moldavets.tiktok_telegram_bot.model.Impl.TelegramUser;

public interface TelegramUserService {

    TelegramUser getById(Long id);

}
