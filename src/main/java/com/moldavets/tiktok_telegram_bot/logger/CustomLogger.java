package com.moldavets.tiktok_telegram_bot.logger;

public interface CustomLogger {
    void info(String message);
    void warn(String message);
    void error(String message);
}
