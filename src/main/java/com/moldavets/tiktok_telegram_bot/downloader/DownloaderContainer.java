package com.moldavets.tiktok_telegram_bot.downloader;

import com.moldavets.tiktok_telegram_bot.command.Command;
import com.moldavets.tiktok_telegram_bot.downloader.Impl.TikTokDownloader;

import java.util.Map;

public class DownloaderContainer {

    private final Map<String, Downloader> COMMAND_MAP;

    public DownloaderContainer() {
        this.COMMAND_MAP = Map.of(
                "TIKTOK", new TikTokDownloader()
        );
    }
}
