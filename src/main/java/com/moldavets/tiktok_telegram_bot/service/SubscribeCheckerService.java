package com.moldavets.tiktok_telegram_bot.service;

public interface SubscribeCheckerService {
    boolean isSubscribed(String chatId);
}
