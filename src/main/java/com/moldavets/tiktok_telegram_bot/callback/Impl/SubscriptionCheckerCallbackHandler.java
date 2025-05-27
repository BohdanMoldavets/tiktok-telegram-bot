package com.moldavets.tiktok_telegram_bot.callback.Impl;

import com.moldavets.tiktok_telegram_bot.bot.TelegramBot;
import com.moldavets.tiktok_telegram_bot.callback.CallbackHandler;
import com.moldavets.tiktok_telegram_bot.logger.Impl.TelegramCustomLogger;
import com.moldavets.tiktok_telegram_bot.model.TelegramChannelStatus;
import com.moldavets.tiktok_telegram_bot.service.TelegramChannelService;
import com.moldavets.tiktok_telegram_bot.service.TelegramUserService;
import com.moldavets.tiktok_telegram_bot.utils.MessageText;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.Objects;

public class SubscriptionCheckerCallbackHandler implements CallbackHandler {

    private final TelegramBot telegramBot;

    private final TelegramUserService telegramUserService;
    private final TelegramChannelService telegramChannelService;

    public SubscriptionCheckerCallbackHandler(TelegramUserService telegramUserService,
                                              TelegramChannelService telegramChannelService, TelegramBot telegramBot) {
        this.telegramUserService = telegramUserService;
        this.telegramChannelService = telegramChannelService;
        this.telegramBot = telegramBot;
    }

    @Override
    public BotApiMethod<?> handle(CallbackQuery callbackQuery) {
        Long userId = callbackQuery.getFrom().getId();
            boolean result = this.checkSubscriptionToChannels(
                    telegramChannelService.getAllWhereStatusIsActive()
                            .stream()
                            .map(channel -> channel.getId().toString())
                            .toList(),
                    userId
            );

        return this.handleResult(result, userId, callbackQuery.getMessage().getMessageId());
    }

    private boolean checkSubscriptionToChannels(List<String> channelIds, Long userId) {
        for(String channelId : channelIds) {
            try {
                ChatMember member = telegramBot.execute(new GetChatMember(channelId, userId));

                if(Objects.equals(member.getStatus(), "left") || Objects.equals(member.getStatus(), "kicked")) {
                    return false;
                }
            } catch (TelegramApiException e) {
                telegramChannelService.updateStatusById(Long.parseLong(channelId), TelegramChannelStatus.INACTIVE);
                return false;
            }
        }
        return true;
    }

    private SendMessage handleResult(boolean result, Long userId, Integer messageId) {
        try {
            if(result) {
                telegramBot.execute(new DeleteMessage(userId.toString(), messageId));
                telegramUserService.updateSubscribeById(userId, true);
                return new SendMessage(userId.toString(), MessageText.CALLBACK_SUCCESSFUL_SUBSCRIPTION.getMessageText());
            }
        } catch (TelegramApiException e) {
            TelegramCustomLogger.getInstance().error("userId " + userId + "|exception " + e.getMessage());
        }
        return SendMessage.builder()
                .chatId(userId)
                .text(MessageText.CALLBACK_FAILED_SUBSCRIPTION.getMessageText())
                .parseMode("html")
                .build();
    }

}
