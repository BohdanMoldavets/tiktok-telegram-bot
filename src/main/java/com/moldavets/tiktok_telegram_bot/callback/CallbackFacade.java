package com.moldavets.tiktok_telegram_bot.callback;

import com.moldavets.tiktok_telegram_bot.callback.Impl.SubscriptionCheckerCallbackHandler;

import java.util.Map;

public class CallbackFacade {

    private final Map<String, CallbackHandler> CALLBACK_HANDLER_MAP;

    public CallbackFacade() {
        this.CALLBACK_HANDLER_MAP = Map.of(
                "CHECK_SUBSCRIPTION_ON_REQUIRED_CHANNELS", new SubscriptionCheckerCallbackHandler()
        );
    }

    public CallbackHandler processCallback(String callbackData) {
        return CALLBACK_HANDLER_MAP.getOrDefault(callbackData, null);
    }
}
