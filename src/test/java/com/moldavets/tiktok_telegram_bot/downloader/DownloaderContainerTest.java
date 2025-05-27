package com.moldavets.tiktok_telegram_bot.downloader;

import com.moldavets.tiktok_telegram_bot.bot.TelegramBot;
import com.moldavets.tiktok_telegram_bot.downloader.Impl.TikTokDownloader;
import com.moldavets.tiktok_telegram_bot.downloader.Impl.UnknownDownloader;
import com.moldavets.tiktok_telegram_bot.service.TelegramChannelService;
import com.moldavets.tiktok_telegram_bot.service.TelegramUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DownloaderContainerTest {

    @Mock
    TelegramUserService telegramUserService;

    @Mock
    TelegramChannelService telegramChannelService;

    @Mock
    TelegramBot telegramBot;

    DownloaderContainer downloaderContainer;

    @BeforeEach
    void setUp() {
        downloaderContainer = new DownloaderContainer(telegramUserService, telegramChannelService, telegramBot);
    }

    @Test
    void processDownloader_shouldReturnTikTokDownloader_whenLinkMatchesTikTokRegex() {
        Downloader actual = downloaderContainer.processDownloader("https://vm.tiktok.com/ZMtKLjnH/");
        assertNotNull(actual);
        assertInstanceOf(TikTokDownloader.class, actual);
    }

    @Test
    void processDownloader_shouldReturnUnknownDownloader_whenLinkNotMatchesAnyRegex() {
        Downloader actual = downloaderContainer.processDownloader("testLink");
        assertNotNull(actual);
        assertInstanceOf(UnknownDownloader.class, actual);
    }
}