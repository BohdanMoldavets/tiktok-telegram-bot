package com.moldavets.tiktok_telegram_bot.callback.Impl;

import com.moldavets.tiktok_telegram_bot.bot.TelegramBot;
import com.moldavets.tiktok_telegram_bot.model.Impl.TelegramChannel;
import com.moldavets.tiktok_telegram_bot.model.TelegramChannelStatus;
import com.moldavets.tiktok_telegram_bot.service.TelegramChannelService;
import com.moldavets.tiktok_telegram_bot.service.TelegramUserService;
import com.moldavets.tiktok_telegram_bot.utils.MessageText;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMemberBanned;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMemberLeft;
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

    private List<TelegramChannel> activeChannels;
    private CallbackQuery callbackQuery;
    private Message message;
    private User user;

    @BeforeEach
    void setUp() {
        subscriptionCheckerCallbackHandler = new SubscriptionCheckerCallbackHandler(telegramUserService, telegramChannelService, telegramBot);
        activeChannels = List.of(
                new TelegramChannel(1L, "https://example.com", TelegramChannelStatus.ACTIVE),
                new TelegramChannel(2L, "https://example2.com", TelegramChannelStatus.ACTIVE),
                new TelegramChannel(3L, "https://example3.com", TelegramChannelStatus.ACTIVE)
        );
        callbackQuery = new CallbackQuery();
        message = new Message();
        user = new User();

        message.setMessageId(654);
        callbackQuery.setMessage(message);
        user.setId(123L);
        callbackQuery.setFrom(user);
    }

    @Test
    void handle_shouldCheckUserSubscriptionsForRequiredChannels_whenInputDataIsValid() throws TelegramApiException {
        Mockito.when(telegramChannelService.getAllWhereStatusIsActive()).thenReturn(activeChannels);
        Mockito.when(telegramBot.execute(any(GetChatMember.class)))
                .thenReturn(new ChatMemberMember(user))
                .thenReturn(new ChatMemberMember(user))
                .thenReturn(new ChatMemberMember(user));
        Mockito.doNothing().when(telegramBot).execute(any(DeleteMessage.class));
        Mockito.doNothing().when(telegramUserService).updateSubscribeById(anyLong(), anyBoolean());

        SendMessage actual = (SendMessage) subscriptionCheckerCallbackHandler.handle(callbackQuery);

        Assertions.assertEquals(user.getId().toString(), actual.getChatId());
        Assertions.assertEquals(MessageText.CALLBACK_SUCCESSFUL_SUBSCRIPTION.getMessageText(), actual.getText());

        Mockito.verify(telegramChannelService, Mockito.times(1))
                .getAllWhereStatusIsActive();
        Mockito.verify(telegramBot, Mockito.times(activeChannels.size()))
                .execute(any(GetChatMember.class));
        Mockito.verify(telegramBot, Mockito.times(1))
                .execute(any(DeleteMessage.class));
        Mockito.verify(telegramUserService, Mockito.times(1))
                .updateSubscribeById(anyLong(), anyBoolean());
    }

    @Test
    void handle_shouldReturnFailedSubscriptionMessage_whenUserLeftFromAtLeastOneRequiredChannel () throws TelegramApiException {
        Mockito.when(telegramChannelService.getAllWhereStatusIsActive()).thenReturn(activeChannels);
        Mockito.when(telegramBot.execute(any(GetChatMember.class)))
                .thenReturn(new ChatMemberMember(user))
                .thenReturn(new ChatMemberLeft(user))
                .thenReturn(new ChatMemberMember(user));

        SendMessage actual = (SendMessage) subscriptionCheckerCallbackHandler.handle(callbackQuery);

        Assertions.assertEquals(user.getId().toString(), actual.getChatId());
        Assertions.assertEquals(MessageText.CALLBACK_FAILED_SUBSCRIPTION.getMessageText(), actual.getText());

        Mockito.verify(telegramChannelService, Mockito.times(1))
                .getAllWhereStatusIsActive();
        Mockito.verify(telegramBot, Mockito.times(2))
                .execute(any(GetChatMember.class));
    }

    @Test
    void handle_shouldReturnFailedSubscriptionMessage_whenUserKickedFromAtLeastOneRequiredChannel () throws TelegramApiException {
        Mockito.when(telegramChannelService.getAllWhereStatusIsActive()).thenReturn(activeChannels);
        Mockito.when(telegramBot.execute(any(GetChatMember.class)))
                .thenReturn(new ChatMemberBanned(user, 0)) // Date when restrictions will be lifted for this user; Unix time. If 0, then the user is banned forever
                .thenReturn(new ChatMemberMember(user))
                .thenReturn(new ChatMemberMember(user));

        SendMessage actual = (SendMessage) subscriptionCheckerCallbackHandler.handle(callbackQuery);

        Assertions.assertEquals(user.getId().toString(), actual.getChatId());
        Assertions.assertEquals(MessageText.CALLBACK_FAILED_SUBSCRIPTION.getMessageText(), actual.getText());

        Mockito.verify(telegramChannelService, Mockito.times(1))
                .getAllWhereStatusIsActive();
        Mockito.verify(telegramBot, Mockito.times(1))
                .execute(any(GetChatMember.class));
    }

    @Test
    void handle_shouldReturnFailedSubscriptionMessage_whenChannelKickedTelegramBotWhichTryingToGetChatMember () throws TelegramApiException {
        Mockito.when(telegramChannelService.getAllWhereStatusIsActive()).thenReturn(activeChannels);
        Mockito.when(telegramBot.execute(any(GetChatMember.class)))
                .thenReturn(new ChatMemberMember(user))
                .thenReturn(new ChatMemberMember(user))
                .thenThrow(TelegramApiException.class);

        Mockito.doNothing().when(telegramChannelService).updateStatusById(anyLong(), any(TelegramChannelStatus.class));

        SendMessage actual = (SendMessage) subscriptionCheckerCallbackHandler.handle(callbackQuery);

        Assertions.assertEquals(user.getId().toString(), actual.getChatId());
        Assertions.assertEquals(MessageText.CALLBACK_FAILED_SUBSCRIPTION.getMessageText(), actual.getText());

        Mockito.verify(telegramChannelService, Mockito.times(1))
                .getAllWhereStatusIsActive();
        Mockito.verify(telegramBot, Mockito.times(activeChannels.size()))
                .execute(any(GetChatMember.class));
    }
}