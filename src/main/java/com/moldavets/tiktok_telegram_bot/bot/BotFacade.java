package com.moldavets.tiktok_telegram_bot.bot;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class BotFacade {

    public BotApiMethod<?> processUpdate(Update update) {
        if(update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText().trim();
            if(message.equals("/start")) {
                return new SendMessage(update.getMessage().getChatId().toString(), "hello");
            }
        }
        return null;
    }
}
