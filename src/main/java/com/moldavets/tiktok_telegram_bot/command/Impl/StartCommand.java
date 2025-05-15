package com.moldavets.tiktok_telegram_bot.command.Impl;

import com.moldavets.tiktok_telegram_bot.command.Command;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class StartCommand implements Command {
    @Override
    public BotApiMethod<?> execute(Update update) {
        return new SendMessage(
                update.getMessage().getChatId().toString(),
                "Hello!!!"
        );
    }
}
