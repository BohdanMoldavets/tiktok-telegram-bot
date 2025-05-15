package com.moldavets.tiktok_telegram_bot.bot;

import com.moldavets.tiktok_telegram_bot.command.CommandContainer;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class BotFacade {

    private final CommandContainer commandContainer;

    public BotFacade() {
        this.commandContainer = new CommandContainer();
    }

    public BotApiMethod<?> processUpdate(Update update) {
        if(update.hasMessage() && update.getMessage().hasText()) {
            String command = update.getMessage().getText().trim();
            return commandContainer.retrieveCommand(command).execute(update);
        }
        return null;
    }
}
