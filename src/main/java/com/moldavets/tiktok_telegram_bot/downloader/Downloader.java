package com.moldavets.tiktok_telegram_bot.downloader;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface Downloader {
    BotApiMethod<?> execute(Update update);
}
