package com.moldavets.tiktok_telegram_bot.callback.Impl;

import com.moldavets.tiktok_telegram_bot.bot.TelegramBot;
import com.moldavets.tiktok_telegram_bot.callback.CallbackHandler;
import com.moldavets.tiktok_telegram_bot.model.Impl.TelegramChannel;
import com.moldavets.tiktok_telegram_bot.service.TelegramChannelService;
import com.moldavets.tiktok_telegram_bot.service.TelegramUserService;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Objects;

public class SubscriptionCheckerCallbackHandler implements CallbackHandler {

    private final TelegramBot telegramBot;

    private final TelegramUserService telegramUserService;
    private final TelegramChannelService telegramChannelService;

    public SubscriptionCheckerCallbackHandler(TelegramUserService telegramUserService, TelegramChannelService telegramChannelService, TelegramBot telegramBot) {
        this.telegramUserService = telegramUserService;
        this.telegramChannelService = telegramChannelService;
        this.telegramBot = telegramBot;
    }

    @Override
    public BotApiMethod<?> handle(CallbackQuery callbackQuery) {
        boolean result = true;
        for(TelegramChannel telegramChannel : telegramChannelService.getAllWhereStatusIsActive()) {
            try {
                ChatMember member = telegramBot.execute(new GetChatMember(telegramChannel.getId().toString(), callbackQuery.getFrom().getId()));
                if(member.getStatus() == null || Objects.equals(member.getStatus(), "left") || Objects.equals(member.getStatus(), "kicked")) {
                    result = false;
                    break;
                }
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
        if(result) {
            try {
                telegramBot.execute(new DeleteMessage(callbackQuery.getFrom().getId().toString(),callbackQuery.getMessage().getMessageId()));
                telegramUserService.updateSubscribeById(callbackQuery.getFrom().getId(), true);
                return new SendMessage(callbackQuery.getFrom().getId().toString(), "Now you can download tiktok");
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
//        return new AnswerCallbackQuery(String callbackQueryId, String text, Boolean showAlert, String url, Integer cacheTime)
        return new SendMessage(callbackQuery.getFrom().getId().toString(), "Subscription Failed");
    }
}
