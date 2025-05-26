package com.moldavets.tiktok_telegram_bot.downloader.Impl;

import com.moldavets.tiktok_telegram_bot.bot.TelegramBot;
import com.moldavets.tiktok_telegram_bot.logger.Impl.TelegramCustomLogger;
import com.moldavets.tiktok_telegram_bot.model.Impl.TelegramUser;
import com.moldavets.tiktok_telegram_bot.parser.Impl.TikTokParser;
import com.moldavets.tiktok_telegram_bot.parser.Impl.VideoParser;
import com.moldavets.tiktok_telegram_bot.service.Impl.TelegramUserServiceImpl;
import com.moldavets.tiktok_telegram_bot.service.TelegramChannelService;
import com.moldavets.tiktok_telegram_bot.utils.MessageText;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
class TikTokDownloaderTest {

    @Mock
    TelegramUserServiceImpl telegramUserService;

    @Mock
    TelegramChannelService telegramChannelService;

    @Mock
    TelegramBot telegramBot;

    @InjectMocks
    TikTokDownloader tikTokDownloader;

    private TelegramUser telegramUser;
    private Chat chat;
    private Message waitMessage;
    private Message message;
    private Update update;
    private User user;

    @BeforeEach
    void setUp() {
        telegramUser = new TelegramUser(123L, "tester", "member", true);

        chat = new Chat();
        chat.setId(telegramUser.getId());

        waitMessage = new Message();
        waitMessage.setChat(chat);
        waitMessage.setMessageId(321);

        update = new Update();
        message = new Message();
        user = new User();

        user.setId(telegramUser.getId());
        user.setUserName("tester");
        message.setFrom(user);
        message.setText("test");
        update.setMessage(message);
    }

    @Test
    void execute_shouldReturnSendMessageWithVideo_whenInputContainsValidData() throws TelegramApiException {
        try(MockedStatic<TikTokParser> mockedTikTokParser = Mockito.mockStatic(TikTokParser.class);
            MockedStatic<VideoParser> mockedVideoParser = Mockito.mockStatic(VideoParser.class);
            MockedStatic<TelegramCustomLogger> mockedLogger = Mockito.mockStatic(TelegramCustomLogger.class)) {

            TelegramCustomLogger mockTelegramCustomLogger = Mockito.mock(TelegramCustomLogger.class);
            InputStream inputStream = new ByteArrayInputStream("dummy".getBytes());
            InputFile inputFile = new InputFile(inputStream, "video.mp4");

            mockedTikTokParser.when(() -> TikTokParser.parse(anyString())).thenReturn("https://cdn.tiktok.com/video.mp4");
            mockedVideoParser.when(() -> VideoParser.parse(anyString())).thenReturn(inputFile);
            mockedLogger.when(TelegramCustomLogger::getInstance).thenReturn(mockTelegramCustomLogger);
            Mockito.doNothing().when(telegramUserService).checkTelegramUserRegistration(anyLong(), anyString());
            Mockito.when(telegramUserService.getById(anyLong())).thenReturn(telegramUser);
            Mockito.when(telegramBot.executeAndReturn(any(SendMessage.class))).thenReturn(waitMessage);
            Mockito.doNothing().when(telegramBot).executeVideo(any(SendVideo.class));
            Mockito.doNothing().when(telegramBot).execute(any(DeleteMessage.class));

            SendMessage actual = (SendMessage) tikTokDownloader.execute(update);

            Assertions.assertEquals(telegramUser.getId().toString(), actual.getChatId());
            Assertions.assertEquals(MessageText.DOWNLOADER_NEXT_VIDEO_REQUEST.getMessageText(), actual.getText());
        }
    }

    @Test
    void execute_shouldReturnSendMessageWithApologies_whenInputContainsNotValidData() {
        try(MockedStatic<TikTokParser> mockedTikTokParser = Mockito.mockStatic(TikTokParser.class);
            MockedStatic<TelegramCustomLogger> mockedLogger = Mockito.mockStatic(TelegramCustomLogger.class)) {

            TelegramCustomLogger mockTelegramCustomLogger = Mockito.mock(TelegramCustomLogger.class);

            mockedLogger.when(TelegramCustomLogger::getInstance).thenReturn(mockTelegramCustomLogger);
            mockedTikTokParser.when(() -> TikTokParser.parse(anyString())).thenThrow(IOException.class);
            Mockito.doNothing().when(telegramUserService).checkTelegramUserRegistration(anyLong(), anyString());
            Mockito.when(telegramUserService.getById(anyLong())).thenReturn(telegramUser);

            SendMessage actual = (SendMessage) tikTokDownloader.execute(update);

            Assertions.assertEquals(telegramUser.getId().toString(), actual.getChatId());
            Assertions.assertEquals(MessageText.DOWNLOADER_FAIL_WHILE_DOWNLOADING.getMessageText(), actual.getText());
        }
    }

    @Test
    void execute_shouldReturnSendMessageWithSubscribtionRequest_whenInputContainsValidData() {
        telegramUser.setSubscribed(false);

        Mockito.doNothing().when(telegramUserService).checkTelegramUserRegistration(anyLong(), anyString());
        Mockito.when(telegramUserService.getById(anyLong())).thenReturn(telegramUser);

        SendMessage actual = (SendMessage) tikTokDownloader.execute(update);

        Assertions.assertEquals(telegramUser.getId().toString(), actual.getChatId());
        Assertions.assertEquals(MessageText.DOWNLOADER_SUBSCRIPTION_REQUEST.getMessageText(), actual.getText());
    }

    @Test
    void execute_shouldReturnSendMessageWithApologiesWhenRequestVideoTooLong_whenInputContainsValidData() throws TelegramApiException {

        try(MockedStatic<TikTokParser> mockedTikTokParser = Mockito.mockStatic(TikTokParser.class);
            MockedStatic<VideoParser> mockedVideoParser = Mockito.mockStatic(VideoParser.class);
            MockedStatic<TelegramCustomLogger> mockedLogger = Mockito.mockStatic(TelegramCustomLogger.class)) {

            TelegramCustomLogger mockTelegramCustomLogger = Mockito.mock(TelegramCustomLogger.class);
            InputStream inputStream = new ByteArrayInputStream("dummy".getBytes());
            InputFile inputFile = new InputFile(inputStream, "video.mp4");

            mockedTikTokParser.when(() -> TikTokParser.parse(anyString())).thenReturn("https://cdn.tiktok.com/video.mp4");
            mockedVideoParser.when(() -> VideoParser.parse(anyString())).thenReturn(inputFile);
            mockedLogger.when(TelegramCustomLogger::getInstance).thenReturn(mockTelegramCustomLogger);
            Mockito.doNothing().when(telegramUserService).checkTelegramUserRegistration(anyLong(), anyString());
            Mockito.when(telegramUserService.getById(anyLong())).thenReturn(telegramUser);
            Mockito.when(telegramBot.executeAndReturn(any(SendMessage.class))).thenReturn(waitMessage);
            Mockito.doThrow(TelegramApiException.class).when(telegramBot).executeVideo(any(SendVideo.class));

            SendMessage actual = (SendMessage) tikTokDownloader.execute(update);

            Assertions.assertEquals(telegramUser.getId().toString(), actual.getChatId());
            Assertions.assertEquals(MessageText.DOWNLOADER_SOMETHING_WENT_WRONG.getMessageText(), actual.getText());
        }
    }
}