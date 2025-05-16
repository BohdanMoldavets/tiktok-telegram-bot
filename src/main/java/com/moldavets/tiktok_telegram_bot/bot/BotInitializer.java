package com.moldavets.tiktok_telegram_bot.bot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Slf4j
@Component
public class BotInitializer {

    private final TikTokTelegramBot tikTokTelegramBot;

    @Autowired
    public BotInitializer(TikTokTelegramBot tikTokTelegramBot) {
        this.tikTokTelegramBot = tikTokTelegramBot;
    }

    @EventListener({ContextRefreshedEvent.class})
    public void init() throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);

        try {
            telegramBotsApi.registerBot(tikTokTelegramBot);
        } catch (TelegramApiException e) {
//            this.log.error("Error occurred: {}", e.getMessage());
        }
    }
}
