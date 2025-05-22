package com.moldavets.tiktok_telegram_bot.command.Impl;


import com.moldavets.tiktok_telegram_bot.command.Command;
import com.moldavets.tiktok_telegram_bot.logger.Impl.TelegramCustomLogger;
import com.moldavets.tiktok_telegram_bot.utils.MessageText;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class UnknownCommand implements Command {
    @Override
    public BotApiMethod<?> execute(Update update) {
        String userId = update.getMessage().getFrom().getId().toString();
        TelegramCustomLogger.getInstance().error(
                String.format("Unknown command [%s] by user [%s]", update.getMessage().getText().substring(1), userId)
        );
        return new SendMessage(userId, MessageText.COMMAND_UNKNOWN.getMessageText());
    }
}
