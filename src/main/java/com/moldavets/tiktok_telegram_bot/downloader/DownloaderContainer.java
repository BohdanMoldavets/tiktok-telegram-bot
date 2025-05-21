package com.moldavets.tiktok_telegram_bot.downloader;

import com.moldavets.tiktok_telegram_bot.bot.TelegramBot;
import com.moldavets.tiktok_telegram_bot.downloader.Impl.TikTokDownloader;
import com.moldavets.tiktok_telegram_bot.downloader.Impl.UnknownDownloader;
import com.moldavets.tiktok_telegram_bot.service.TelegramChannelService;
import com.moldavets.tiktok_telegram_bot.service.TelegramUserService;

import java.util.Map;

public class DownloaderContainer {

    private final String LINK_REGEX = "https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)";
    private final Map<String, Downloader> DOWNLOADER_MAP;

    public DownloaderContainer(TelegramUserService telegramUserService, TelegramChannelService telegramChannelService, TelegramBot telegramBot) {
        this.DOWNLOADER_MAP = Map.of(
                "TIKTOK", new TikTokDownloader(telegramUserService, telegramChannelService, telegramBot),
                "UNKNOWN", new UnknownDownloader()
        );
    }

    public Downloader processDownloader(String link) {
        if(link.matches(LINK_REGEX)) {
            return DOWNLOADER_MAP.get("TIKTOK");
        }
        return DOWNLOADER_MAP.get("UNKNOWN"); //todo change regex, when new downloader will be added
    }
}
