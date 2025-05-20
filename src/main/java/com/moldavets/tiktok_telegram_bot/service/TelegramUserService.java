package com.moldavets.tiktok_telegram_bot.service;

import com.moldavets.tiktok_telegram_bot.model.Impl.TelegramUser;
import com.moldavets.tiktok_telegram_bot.model.TelegramUserStatus;

public interface TelegramUserService {

    TelegramUser getById(Long id);

    void checkTelegramUserRegistration(Long userId, String username);

    void save(TelegramUser telegramUser);

    void updateStatusById(Long id, TelegramUserStatus telegramUserStatus);

    void updateSubscriptionForAllUsers(boolean isSubscribed);

    void updateSubscribeById(Long userId, Boolean subscribe);

    void updateLastModifiedDateById(Long id);
}
