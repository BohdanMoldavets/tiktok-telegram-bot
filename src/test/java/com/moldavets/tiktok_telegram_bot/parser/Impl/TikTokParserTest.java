package com.moldavets.tiktok_telegram_bot.parser.Impl;

import com.moldavets.tiktok_telegram_bot.logger.Impl.TelegramCustomLogger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TikTokParserTest {

    private static String API_URL;
    private static String testUrl;
    private static String expectedLink;

    @BeforeAll
    static void setUp() {
        API_URL = "https://ttsave.app/download";
        testUrl = "https://www.tiktok.com/@test/video/123456";
        expectedLink = "https://cdn.tiktok.com/video.mp4";
    }

    @Test
    void parse_shouldParseTikTokVideoLink_whenInputContainsValidData() throws IOException {
        try (MockedStatic<Jsoup> jsoupMock = Mockito.mockStatic(Jsoup.class)) {

            Connection connectionMock = Mockito.mock(Connection.class);
            Document documentMock = Mockito.mock(Document.class);
            Elements elementsMock = Mockito.mock(Elements.class);

            jsoupMock.when(() -> Jsoup.connect(API_URL)).thenReturn(connectionMock);
            Mockito.when(connectionMock.data("query", testUrl)).thenReturn(connectionMock);
            Mockito.when(connectionMock.data("language_id", "1")).thenReturn(connectionMock);
            Mockito.when(connectionMock.post()).thenReturn(documentMock);
            Mockito.when(documentMock.select("a[href]")).thenReturn(elementsMock);
            Mockito.when(elementsMock.attr("abs:href")).thenReturn(expectedLink);

            String actual = TikTokParser.parse(testUrl);

            assertEquals(expectedLink, actual);
        }
    }

    @Test
    void parse_shouldThrowException_whenApiUrlDoesNotWork() throws IOException {

        try (MockedStatic<Jsoup> jsoupMock = Mockito.mockStatic(Jsoup.class);
             MockedStatic<TelegramCustomLogger> mockedLogger = Mockito.mockStatic(TelegramCustomLogger.class)) {

            TelegramCustomLogger telegramCustomLogger = Mockito.mock(TelegramCustomLogger.class);

            mockedLogger.when(TelegramCustomLogger::getInstance).thenReturn(telegramCustomLogger);
            jsoupMock.when(() -> Jsoup.connect(API_URL)).thenReturn(null);

            assertThrows(IOException.class, () -> TikTokParser.parse(testUrl));
        }
    }

    @Test
    void parse_shouldThrowException_whenRequestContainsNotExistUrl() throws IOException {
        try (MockedStatic<Jsoup> jsoupMock = Mockito.mockStatic(Jsoup.class);
             MockedStatic<TelegramCustomLogger> mockedLogger = Mockito.mockStatic(TelegramCustomLogger.class)) {

            TelegramCustomLogger telegramCustomLogger = Mockito.mock(TelegramCustomLogger.class);
            Connection connectionMock = Mockito.mock(Connection.class);

            mockedLogger.when(TelegramCustomLogger::getInstance).thenReturn(telegramCustomLogger);
            jsoupMock.when(() -> Jsoup.connect(API_URL)).thenReturn(connectionMock);
            Mockito.when(connectionMock.data("query", testUrl)).thenReturn(connectionMock);
            Mockito.when(connectionMock.data("language_id", "1")).thenReturn(connectionMock);
            Mockito.when(connectionMock.post()).thenThrow(IOException.class);

            assertThrows(IOException.class, () -> TikTokParser.parse(testUrl));
        }
    }
}