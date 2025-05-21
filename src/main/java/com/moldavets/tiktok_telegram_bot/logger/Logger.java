package com.moldavets.tiktok_telegram_bot.logger;

import com.moldavets.tiktok_telegram_bot.bot.TelegramBot;

public interface Logger {
    void info(String message);
    void warn(String message);
    void error(String message);
}
