package com.moldavets.tiktok_telegram_bot.logger.Impl;

import com.moldavets.tiktok_telegram_bot.bot.TelegramBot;
import com.moldavets.tiktok_telegram_bot.logger.Logger;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class TelegramLogger implements Logger {

    private static TelegramLogger instance;

    private final Long LOG_CHAT_ID;
    private final TelegramBot telegramBot;

    public TelegramLogger(TelegramBot telegramBot, Long logChatId) {
        this.telegramBot = telegramBot;
        this.LOG_CHAT_ID = logChatId;
    }

    public static void init(TelegramBot telegramBot, Long logChat) {
        if(instance == null) {
            instance = new TelegramLogger(telegramBot, logChat);
        }
    }

    public static TelegramLogger getInstance() {
        return instance;
    }

    @Override
    public void info(String message) {
        try {
            telegramBot.execute(
                    new SendMessage(
                            LOG_CHAT_ID.toString(),
                            message
                    )
            );
        } catch (TelegramApiException e) {
            throw new RuntimeException(e); //todo
        }
    }

    @Override
    public void warn(String message) {

    }

    @Override
    public void error(String message) {

    }
}
