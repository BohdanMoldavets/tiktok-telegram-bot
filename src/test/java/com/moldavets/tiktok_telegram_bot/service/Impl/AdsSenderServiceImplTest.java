package com.moldavets.tiktok_telegram_bot.service.Impl;

import com.moldavets.tiktok_telegram_bot.bot.TelegramBot;
import com.moldavets.tiktok_telegram_bot.model.Impl.TelegramUser;
import com.moldavets.tiktok_telegram_bot.service.TelegramUserService;
import com.moldavets.tiktok_telegram_bot.utils.MessageText;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.Video;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AdsSenderServiceImplTest {

    @Mock
    TelegramBot telegramBot;

    @Mock
    TelegramUserService telegramUserService;

    AdsSenderServiceImpl adsSenderService;

    @BeforeEach
    void setUp() {
        this.adsSenderService = new AdsSenderServiceImpl(telegramBot, telegramUserService, 123L);
    }

    @Test
    void sendForwardMessageToAllUsers_shouldSendTextMessageToAllUsers_whenDbContainsAtLeastOneTelegramUser() throws TelegramApiException {
        Message message = new Message();
        message.setText("test");

        Update update = new Update();
        update.setMessage(message);
        update.setChannelPost(message);

        Set<TelegramUser> telegramUsers = Set.of(
                new TelegramUser(1L, "test1", "member", true),
                new TelegramUser(2L, "test2", "member", true),
                new TelegramUser(3L, "test3", "member", true)
        );

        Mockito.when(telegramUserService.getAllActiveTelegramUsers()).thenReturn(telegramUsers);
        Mockito.doNothing().when(telegramBot).execute(Mockito.any(SendMessage.class));

        String expected = MessageText.ADS_AMOUNT_OF_USERS_WHO_RECEIVED_AD.getMessageText() + " - " + telegramUsers.size();
        String actual = adsSenderService.sendForwardMessageToAllUsers(update).getText();

        Assertions.assertEquals(expected, actual);
        Mockito.verify(telegramUserService, Mockito.times(1)).getAllActiveTelegramUsers();
        Mockito.verify(telegramBot, Mockito.times(3)).execute(Mockito.any(SendMessage.class));
    }

    @Test
    void sendForwardMessageToAllUsers_shouldSendPhotoMessageToAllUsers_whenDbContainsAtLeastOneTelegramUser() throws TelegramApiException {
        Message message = new Message();
        PhotoSize photoSize = new PhotoSize();
        photoSize.setFileId("fileId");
        message.setPhoto(List.of(photoSize));

        Update update = new Update();
        update.setMessage(message);
        update.setChannelPost(message);

        Set<TelegramUser> telegramUsers = Set.of(
                new TelegramUser(1L, "test1", "member", true),
                new TelegramUser(2L, "test2", "member", true),
                new TelegramUser(3L, "test3", "member", true)
        );

        Mockito.when(telegramUserService.getAllActiveTelegramUsers()).thenReturn(telegramUsers);
        Mockito.doNothing().when(telegramBot).executePhoto(Mockito.any(SendPhoto.class));

        String expected = MessageText.ADS_AMOUNT_OF_USERS_WHO_RECEIVED_AD.getMessageText() + " - " + telegramUsers.size();
        String actual = adsSenderService.sendForwardMessageToAllUsers(update).getText();

        Assertions.assertEquals(expected, actual);
        Mockito.verify(telegramUserService, Mockito.times(1)).getAllActiveTelegramUsers();
        Mockito.verify(telegramBot, Mockito.times(3)).executePhoto(Mockito.any(SendPhoto.class));
    }

    @Test
    void sendForwardMessageToAllUsers_shouldSendVideoMessageToAllUsers_whenDbContainsAtLeastOneTelegramUser() throws TelegramApiException {
        Message message = new Message();
        Video video = new Video();
        video.setFileId("fileId");
        message.setVideo(video);

        Update update = new Update();
        update.setMessage(message);
        update.setChannelPost(message);

        Set<TelegramUser> telegramUsers = Set.of(
                new TelegramUser(1L, "test1", "member", true),
                new TelegramUser(2L, "test2", "member", true),
                new TelegramUser(3L, "test3", "member", true)
        );

        Mockito.when(telegramUserService.getAllActiveTelegramUsers()).thenReturn(telegramUsers);
        Mockito.doNothing().when(telegramBot).executeVideo(Mockito.any(SendVideo.class));

        String expected = MessageText.ADS_AMOUNT_OF_USERS_WHO_RECEIVED_AD.getMessageText() + " - " + telegramUsers.size();
        String actual = adsSenderService.sendForwardMessageToAllUsers(update).getText();

        Assertions.assertEquals(expected, actual);
        Mockito.verify(telegramUserService, Mockito.times(1)).getAllActiveTelegramUsers();
        Mockito.verify(telegramBot, Mockito.times(3)).executeVideo(Mockito.any(SendVideo.class));
    }

//    @Test
//    void sendForwardMessageToAllUsers_shouldHandleTelegramApiException_whenUserCannotGetMessage() throws TelegramApiException {
//        Message message = new Message();
//        message.setText("test");
//
//        Update update = new Update();
//        update.setMessage(message);
//        update.setChannelPost(message);
//
//        Set<TelegramUser> telegramUsers = Set.of(
//                new TelegramUser(1L, "test1", "member", true),
//                new TelegramUser(2L, "test2", "member", true),
//                new TelegramUser(3L, "test3", "member", true)
//        );
//
//        Mockito.when(telegramUserService.getAllActiveTelegramUsers()).thenReturn(telegramUsers);
//        Mockito.doThrow(TelegramApiException.class).when(telegramBot).execute(Mockito.any(SendMessage.class));
//    }

}