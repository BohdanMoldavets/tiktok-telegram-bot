package com.moldavets.tiktok_telegram_bot.parser.Impl;

import com.moldavets.tiktok_telegram_bot.logger.Impl.TelegramCustomLogger;
import com.moldavets.tiktok_telegram_bot.parser.Parser;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.io.IOException;

public class TikTokParser implements Parser {

    public static String parse(String videoUrl) throws IOException {
        if(videoUrl == null || videoUrl.trim().isEmpty()) {
            throw new NullPointerException("Url cannot be null or empty");
        }

        try {
            Elements elements = Jsoup.connect("https://ttsave.app/download")
                    .data("query", videoUrl)
                    .data("language_id", "1")
                    .post().select("a[href]");
            return elements.attr("abs:href");
        } catch (Exception e) {
            TelegramCustomLogger.getInstance().error(
                    String.format("Couldn't parse tiktok Url[%s] exception[%s] ", videoUrl, e));
            throw new IOException(e);
        }
    }
}
