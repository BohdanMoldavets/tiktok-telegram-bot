package com.moldavets.tiktok_telegram_bot.command.Impl;

import com.moldavets.tiktok_telegram_bot.service.TelegramUserService;
import com.moldavets.tiktok_telegram_bot.utils.MessageText;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
class StartCommandTest {

    @Mock
    TelegramUserService telegramUserService;

    @InjectMocks
    StartCommand startCommand;

    @Test
    void execute_shouldReturnSendMessageWithStartCommandText_whenInputContainsValidData() {
        Update update = new Update();
        Message message = new Message();
        User user = new User();

        user.setId(123L);
        user.setUserName("tester");
        message.setFrom(user);
        update.setMessage(message);

        Mockito.doNothing().when(telegramUserService).checkTelegramUserRegistration(anyLong(), anyString());

        SendMessage actual = (SendMessage) startCommand.execute(update);

        assertEquals(user.getId().toString(), actual.getChatId());
        assertEquals(MessageText.COMMAND_START.getMessageText(), actual.getText());
    }

}