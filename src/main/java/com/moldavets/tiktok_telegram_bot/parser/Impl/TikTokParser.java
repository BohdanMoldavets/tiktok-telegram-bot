package com.moldavets.tiktok_telegram_bot.parser.Impl;

import com.moldavets.tiktok_telegram_bot.parser.Parser;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.io.IOException;

public class TikTokParser implements Parser {

    public static String parse(String videoUrl) throws IOException {
        try {
            Elements elements = Jsoup.connect("https://ttsave.app/download")
                    .data("query", videoUrl)
                    .data("language_id", "1")
                    .post().select("a[href]");
            return elements.attr("abs:href");
        } catch (Exception e) {
            throw new IOException(e);
        }
    }
}
