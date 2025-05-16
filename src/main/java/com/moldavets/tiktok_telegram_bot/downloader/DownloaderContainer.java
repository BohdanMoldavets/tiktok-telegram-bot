package com.moldavets.tiktok_telegram_bot.downloader;

import com.moldavets.tiktok_telegram_bot.downloader.Impl.TikTokDownloader;
import com.moldavets.tiktok_telegram_bot.service.TelegramChannelService;
import com.moldavets.tiktok_telegram_bot.service.TelegramUserService;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

import java.util.Map;

public class DownloaderContainer {

    private final Map<String, Downloader> COMMAND_MAP;

    public DownloaderContainer(TelegramUserService telegramUserService, TelegramChannelService telegramChannelService) {
        this.COMMAND_MAP = Map.of(
                "TIKTOK", new TikTokDownloader(telegramUserService, telegramChannelService)
        );
    }

    public BotApiMethod<?> processDownloader(Downloader downloader) {
        return null; //todo retrieve downloader based on regex
    }
}
