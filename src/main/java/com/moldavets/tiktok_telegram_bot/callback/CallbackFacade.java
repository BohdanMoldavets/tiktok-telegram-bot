package com.moldavets.tiktok_telegram_bot.callback;

import com.moldavets.tiktok_telegram_bot.bot.TelegramBot;
import com.moldavets.tiktok_telegram_bot.callback.Impl.SubscriptionCheckerCallbackHandler;
import com.moldavets.tiktok_telegram_bot.service.TelegramChannelService;
import com.moldavets.tiktok_telegram_bot.service.TelegramUserService;

import java.util.Map;

public class CallbackFacade {

    private final Map<String, CallbackHandler> CALLBACK_HANDLER_MAP;

    public CallbackFacade(TelegramUserService telegramUserService, TelegramChannelService telegramChannelService, TelegramBot telegramBot) {
        this.CALLBACK_HANDLER_MAP = Map.of(
                "CHECK_SUBSCRIPTION_ON_REQUIRED_CHANNELS", new SubscriptionCheckerCallbackHandler(telegramUserService, telegramChannelService, telegramBot)
        );
    }

    public CallbackHandler processCallback(String callbackData) {
        return CALLBACK_HANDLER_MAP.getOrDefault(callbackData, null);
    }
}
