package com.moldavets.tiktok_telegram_bot.downloader.Impl;

import com.moldavets.tiktok_telegram_bot.logger.Impl.TelegramCustomLogger;
import com.moldavets.tiktok_telegram_bot.utils.MessageText;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

@ExtendWith(MockitoExtension.class)
class UnknownDownloaderTest {

    @InjectMocks
    UnknownDownloader unknownDownloader;

    @Test
    void execute_shouldReturnSendMessageWithUnknownDownloaderText_whenCustomerProvideInvalidDownloaderLink() {
        Update update = new Update();
        Message message = new Message();
        User user = new User();

        user.setId(123L);
        message.setFrom(user);
        update.setMessage(message);

        try(MockedStatic<TelegramCustomLogger> mockedLogger = Mockito.mockStatic(TelegramCustomLogger.class)) {
            TelegramCustomLogger mockTelegramCustomLogger = Mockito.mock(TelegramCustomLogger.class);

            mockedLogger.when(TelegramCustomLogger::getInstance).thenReturn(mockTelegramCustomLogger);

            SendMessage actual = (SendMessage) unknownDownloader.execute(update);

            Assertions.assertEquals(user.getId().toString(), actual.getChatId());
            Assertions.assertEquals(MessageText.DOWNLOADER_UNKNOWN.getMessageText(), actual.getText());
        }
    }

}