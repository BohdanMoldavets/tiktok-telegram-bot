package com.moldavets.tiktok_telegram_bot.command.Impl;


import com.moldavets.tiktok_telegram_bot.command.Command;
import com.moldavets.tiktok_telegram_bot.utils.MessageText;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class UnknownCommand implements Command {
    @Override
    public BotApiMethod<?> execute(Update update) {
        return new SendMessage(update.getMessage().getFrom().getId().toString(), MessageText.COMMAND_UNKNOWN.getMessageText());
    }
}
