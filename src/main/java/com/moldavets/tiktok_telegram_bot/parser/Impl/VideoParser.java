package com.moldavets.tiktok_telegram_bot.parser.Impl;

import com.moldavets.tiktok_telegram_bot.parser.Parser;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public final class VideoParser implements Parser {

    private VideoParser() {}

    public static InputFile parse(String url) throws IOException {
        try {
            URL destinationUrl = new URL(url);

            HttpURLConnection connection = (HttpURLConnection) destinationUrl.openConnection();
            connection.setRequestMethod("GET");
            InputStream inputStream = connection.getInputStream();

            return new InputFile(inputStream, "video.mp4");
        } catch (IOException e) {
            throw new IOException(e);
        }
    }
}
