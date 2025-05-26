package com.moldavets.tiktok_telegram_bot.parser.Impl;

import com.moldavets.tiktok_telegram_bot.logger.Impl.TelegramCustomLogger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import static org.junit.jupiter.api.Assertions.*;

class VideoParserTest {

    @Test
    void parse_shouldReturnInputStream_whenVideoUrlIsValid() throws IOException {
        String videoUrl = "https://cdn.tiktok.com/video.mp4";

        HttpURLConnection connectionMock = Mockito.mock(HttpURLConnection.class);
        InputStream inputStream = new ByteArrayInputStream("dummy".getBytes());


        try (MockedStatic<VideoParser> mockedVideoParser = Mockito.mockStatic(VideoParser.class, Mockito.CALLS_REAL_METHODS)) {
            Mockito.when(connectionMock.getInputStream()).thenReturn(inputStream);

            mockedVideoParser.when(() -> VideoParser.openConnection(videoUrl)).thenReturn(connectionMock);

            InputFile actual = VideoParser.parse(videoUrl);

            assertNotNull(actual);
            assertEquals("video.mp4", actual.getMediaName());
            assertEquals(inputStream, actual.getNewMediaStream());
        }
    }

    @Test
    void parse_shouldThrowException_whenConnectionFails() {
        String url = "https://example.com/video.mp4";

        try (MockedStatic<VideoParser> mockedVideoParser = Mockito.mockStatic(VideoParser.class, Mockito.CALLS_REAL_METHODS);
             MockedStatic<TelegramCustomLogger> mockedLogger = Mockito.mockStatic(TelegramCustomLogger.class)) {

            TelegramCustomLogger telegramCustomLogger = Mockito.mock(TelegramCustomLogger.class);

            mockedVideoParser.when(() -> VideoParser.openConnection(url)).thenThrow(IOException.class);
            mockedLogger.when(TelegramCustomLogger::getInstance).thenReturn(telegramCustomLogger);

            assertThrows(IOException.class, () -> VideoParser.parse(url));
        }
    }

    @Test
    void parse_shouldThrowException_whenInputContainsEmptyString() {
        assertThrows(NullPointerException.class, () -> VideoParser.parse(""));
    }

    @Test
    void parse_shouldThrowException_whenInputContainsStringWithOnlySpaces() {
        assertThrows(NullPointerException.class, () -> VideoParser.parse("  "));
    }

    @Test
    void parse_shouldThrowException_whenInputContainsNull() {
        assertThrows(NullPointerException.class, () -> VideoParser.parse(null));
    }

}