package com.moldavets.tiktok_telegram_bot.callback;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

public interface CallbackHandler {
    BotApiMethod<?> handle(CallbackQuery callbackQuery);
}
