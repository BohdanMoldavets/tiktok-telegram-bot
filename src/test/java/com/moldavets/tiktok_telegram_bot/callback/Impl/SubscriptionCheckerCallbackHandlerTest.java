package com.moldavets.tiktok_telegram_bot.callback.Impl;

import com.moldavets.tiktok_telegram_bot.bot.TelegramBot;
import com.moldavets.tiktok_telegram_bot.model.Impl.TelegramChannel;
import com.moldavets.tiktok_telegram_bot.model.TelegramChannelStatus;
import com.moldavets.tiktok_telegram_bot.service.TelegramChannelService;
import com.moldavets.tiktok_telegram_bot.service.TelegramUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMemberMember;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
class SubscriptionCheckerCallbackHandlerTest {

    @Mock
    TelegramUserService telegramUserService;

    @Mock
    TelegramChannelService telegramChannelService;

    @Mock
    TelegramBot telegramBot;

    SubscriptionCheckerCallbackHandler subscriptionCheckerCallbackHandler;

    @BeforeEach
    void setUp() {
        subscriptionCheckerCallbackHandler = new SubscriptionCheckerCallbackHandler(telegramUserService, telegramChannelService, telegramBot);
    }

    @Test
    void handle_shouldCheckUserSubscriptionsForRequiredChannels_whenInputDataIsValid() throws TelegramApiException {
        List<TelegramChannel> activeChannels = List.of(
                new TelegramChannel(1L, "https://example.com", TelegramChannelStatus.ACTIVE),
                new TelegramChannel(2L, "https://example2.com", TelegramChannelStatus.ACTIVE),
                new TelegramChannel(3L, "https://example3.com", TelegramChannelStatus.ACTIVE)
        );

        User user1 = new User();
        user1.setId(1L);

        User user2 = new User();
        user2.setId(2L);

        User user3 = new User();
        user3.setId(3L);

        CallbackQuery callbackQuery = new CallbackQuery();
        User user = new User();

        user.setId(123L);
        callbackQuery.setFrom(user);

        Mockito.when(telegramChannelService.getAllWhereStatusIsActive()).thenReturn(activeChannels);
        Mockito.when(telegramBot.execute(any(GetChatMember.class)))
                .thenReturn(new ChatMemberMember(user1))
                .thenReturn(new ChatMemberMember(user2))
                .thenReturn(new ChatMemberMember(user3));

        SendMessage actual = (SendMessage) subscriptionCheckerCallbackHandler.handle(callbackQuery);
        //todo
    }
}