package com.moldavets.tiktok_telegram_bot.downloader.Impl.tiktok;

import com.moldavets.tiktok_telegram_bot.bot.TelegramBot;
import com.moldavets.tiktok_telegram_bot.downloader.Downloader;
import com.moldavets.tiktok_telegram_bot.keyboard.KeyboardContainer;
import com.moldavets.tiktok_telegram_bot.model.Impl.TelegramUser;
import com.moldavets.tiktok_telegram_bot.parser.Impl.VideoParser;
import com.moldavets.tiktok_telegram_bot.service.TelegramChannelService;
import com.moldavets.tiktok_telegram_bot.service.TelegramUserService;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class TikTokDownloader implements Downloader {

    private final String MESSAGE = "Please subscribe on this groups for Download TikTok";

    private TelegramBot telegramBot;

    private final TelegramUserService telegramUserService;
    private final TelegramChannelService telegramChannelService;

    public TikTokDownloader(TelegramUserService telegramUserService, TelegramChannelService telegramChannelService, TelegramBot telegramBot) {
        this.telegramUserService = telegramUserService;
        this.telegramChannelService = telegramChannelService;
        this.telegramBot = telegramBot;
    }

    @Override
    public BotApiMethod<?> execute(Update update) {
        Long userId = update.getMessage().getFrom().getId();
        String username = update.getMessage().getFrom().getUserName();
        telegramUserService.checkTelegramUserRegistration(userId, username);

        TelegramUser storedUser = telegramUserService.getById(userId);
        if (storedUser != null && storedUser.isSubscribed()) {
            try {
                SendVideo sendVideo = new SendVideo(userId.toString(), VideoParser.parse("https://tikcdn.io/tiktokdownload/7309569645982403858"));
                sendVideo.setCaption("Download more tiktoks here TikTok @tiktok");
                telegramBot.executeVideo(sendVideo);

            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }

        SendMessage message = new SendMessage(userId.toString(), MESSAGE);
        message.setReplyMarkup(KeyboardContainer.getChannelsToSubscribeKeyboard(telegramChannelService));
        return message;
    }
}
