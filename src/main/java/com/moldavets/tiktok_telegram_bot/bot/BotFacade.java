package com.moldavets.tiktok_telegram_bot.bot;

import com.moldavets.tiktok_telegram_bot.command.CommandContainer;
import com.moldavets.tiktok_telegram_bot.service.TelegramChannelService;
import com.moldavets.tiktok_telegram_bot.service.TelegramUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class BotFacade {

    private final CommandContainer commandContainer;
    private final String COMMAND_PREFIX = "/";

    @Autowired
    public BotFacade(TelegramUserService telegramUserService, TelegramChannelService telegramChannelService) {
        this.commandContainer = new CommandContainer(telegramUserService, telegramChannelService);
    }

    public BotApiMethod<?> processUpdate(Update update) {
        if(update.hasMessage() && update.getMessage().hasText()) {
            String command = update.getMessage().getText().trim();
            return command.startsWith(COMMAND_PREFIX) ? commandContainer.retrieveCommand(command).execute(update) : null;
        }
        return null;
    }
}
