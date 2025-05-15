package com.moldavets.tiktok_telegram_bot.command;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface Command {
    BotApiMethod<?> execute(Update update);
}
