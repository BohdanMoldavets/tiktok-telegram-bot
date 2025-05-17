package com.moldavets.tiktok_telegram_bot.callback.Impl;

import com.moldavets.tiktok_telegram_bot.callback.CallbackHandler;
import com.moldavets.tiktok_telegram_bot.service.TelegramChannelService;
import com.moldavets.tiktok_telegram_bot.service.TelegramUserService;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

public class SubscriptionCheckerCallbackHandler implements CallbackHandler {

    private final TelegramUserService telegramUserService;
    private final TelegramChannelService telegramChannelService;

    public SubscriptionCheckerCallbackHandler(TelegramUserService telegramUserService, TelegramChannelService telegramChannelService) {
        this.telegramUserService = telegramUserService;
        this.telegramChannelService = telegramChannelService;
    }

    @Override
    public BotApiMethod<?> handle(CallbackQuery callbackQuery) {
        GetChatMember chatMember = new GetChatMember();
        chatMember.setChatId(telegramChannelService.getAllWhereStatusIsActive().getLast().getId());
        chatMember.setUserId(callbackQuery.getFrom().getId());
        return chatMember;
    }
}
