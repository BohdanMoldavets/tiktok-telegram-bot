package com.moldavets.tiktok_telegram_bot.downloader.Impl;

import com.moldavets.tiktok_telegram_bot.downloader.Downloader;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

public class TikTokDownloader implements Downloader {
    @Override
    public BotApiMethod<?> execute(Update update) {
        return null;
    }
}
