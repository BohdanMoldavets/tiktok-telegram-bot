package com.moldavets.tiktok_telegram_bot.logger.Impl;

import com.moldavets.tiktok_telegram_bot.bot.TelegramBot;
import com.moldavets.tiktok_telegram_bot.logger.CustomLogger;
import com.moldavets.tiktok_telegram_bot.logger.LogType;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDateTime;

@Slf4j
public class TelegramCustomLogger implements CustomLogger {

    private static TelegramCustomLogger instance;

    private static final String INFO_MARK = "[INFO] ";
    private static final String WARN_MARK = "[WARN] ";
    private static final String ERROR_MARK = "[ERROR] ";

    private final Long LOG_CHAT_ID;
    private final TelegramBot telegramBot;

    public TelegramCustomLogger(TelegramBot telegramBot, Long logChatId) {
        this.telegramBot = telegramBot;
        this.LOG_CHAT_ID = logChatId;
    }

    public static void init(TelegramBot telegramBot, Long logChat) {
        if(instance == null) {
            instance = new TelegramCustomLogger(telegramBot, logChat);
        }
    }

    public static TelegramCustomLogger getInstance() {
        return instance;
    }

    @Override
    public void info(String message) {
        this.executeLog(message, LogType.INFO);
        log.info(message);
    }

    @Override
    public void warn(String message) {
        this.executeLog(message, LogType.WARN);
        log.warn(message);
    }

    @Override
    public void error(String message) {
        this.executeLog(message, LogType.ERROR);
        log.error(message);
    }

    private void executeLog(String message, LogType logType) {
        try {
            telegramBot.execute(
                    new SendMessage(
                            LOG_CHAT_ID.toString(),
                            LocalDateTime.now() + logType.getLogMark() + message
                    )
            );
        } catch (TelegramApiException e) {
            throw new RuntimeException(e); //todo
        }
    }
}
