package com.moldavets.tiktok_telegram_bot.downloader.Impl;

import com.moldavets.tiktok_telegram_bot.downloader.Downloader;
import com.moldavets.tiktok_telegram_bot.utils.MessageText;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class UnknownDownloader implements Downloader {
    @Override
    public BotApiMethod<?> execute(Update update) {
        return new SendMessage(update.getMessage().getFrom().getId().toString(), MessageText.DOWNLOADER_UNKNOWN.getMessageText());
    }
}
