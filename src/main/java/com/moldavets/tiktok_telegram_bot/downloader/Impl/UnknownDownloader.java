package com.moldavets.tiktok_telegram_bot.downloader.Impl;

import com.moldavets.tiktok_telegram_bot.downloader.Downloader;
import com.moldavets.tiktok_telegram_bot.logger.Impl.TelegramCustomLogger;
import com.moldavets.tiktok_telegram_bot.utils.MessageText;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class UnknownDownloader implements Downloader {
    @Override
    public BotApiMethod<?> execute(Update update) {
        TelegramCustomLogger.getInstance().error(
                String.format("Unknown downloader method user[%s] link[%s]",
                        update.getMessage().getFrom().getId(),
                        update.getMessage().getText()
                )
        );
        return new SendMessage(update.getMessage().getFrom().getId().toString(), MessageText.DOWNLOADER_UNKNOWN.getMessageText());
    }
}
