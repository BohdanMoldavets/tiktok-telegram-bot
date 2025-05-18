package com.moldavets.tiktok_telegram_bot.downloader;

import com.moldavets.tiktok_telegram_bot.bot.TelegramBot;
import com.moldavets.tiktok_telegram_bot.downloader.Impl.TikTokDownloader;
import com.moldavets.tiktok_telegram_bot.service.TelegramChannelService;
import com.moldavets.tiktok_telegram_bot.service.TelegramUserService;

import java.util.Map;

public class DownloaderContainer {

    private final String TIKTOK_LINK_REGEX = "^.*https:\\/\\/(?:m|www|vm)?\\.?tiktok\\.com\\/((?:.*\\b(?:(?:usr|v|embed|user|video)\\/|\\?shareId=|\\&item_id=)(\\d+))|\\w+)";

    private final Map<String, Downloader> DOWNLOADER_MAP;

    public DownloaderContainer(TelegramUserService telegramUserService, TelegramChannelService telegramChannelService, TelegramBot telegramBot) {
        this.DOWNLOADER_MAP = Map.of(
                "TIKTOK", new TikTokDownloader(telegramUserService, telegramChannelService, telegramBot)
        );
    }

    public Downloader processDownloader(String link) {
        if(link.matches(TIKTOK_LINK_REGEX)) {
            return DOWNLOADER_MAP.get("TIKTOK");
        }
        return DOWNLOADER_MAP.get("TIKTOK"); //todo change tiktok regex
    }
}
