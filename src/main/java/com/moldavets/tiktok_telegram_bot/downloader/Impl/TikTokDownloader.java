package com.moldavets.tiktok_telegram_bot.downloader.Impl;

import com.moldavets.tiktok_telegram_bot.bot.TelegramBot;
import com.moldavets.tiktok_telegram_bot.downloader.Downloader;
import com.moldavets.tiktok_telegram_bot.keyboard.KeyboardContainer;
import com.moldavets.tiktok_telegram_bot.model.Impl.TelegramUser;
import com.moldavets.tiktok_telegram_bot.parser.Impl.TikTokParser;
import com.moldavets.tiktok_telegram_bot.parser.Impl.VideoParser;
import com.moldavets.tiktok_telegram_bot.service.TelegramChannelService;
import com.moldavets.tiktok_telegram_bot.service.TelegramUserService;
import com.moldavets.tiktok_telegram_bot.utils.MessageText;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class TikTokDownloader implements Downloader {

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

        TelegramUser storedUser = telegramUserService.getById(userId);
        if (storedUser != null && storedUser.isSubscribed()) {
            try {
                SendVideo sendVideo = new SendVideo(userId.toString(), VideoParser.parse(TikTokParser.parse(update.getMessage().getText())));
                sendVideo.setCaption(MessageText.DOWNLOADER_ASSIGN_CAPTION.getMessageText());
                telegramBot.executeVideo(sendVideo);
                return new SendMessage(userId.toString(), MessageText.DOWNLOADER_NEXT_VIDEO_REQUEST.getMessageText());
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }

        SendMessage message = new SendMessage(userId.toString(), MessageText.DOWNLOADER_SUBSCRIPTION_REQUEST.getMessageText());
        message.setReplyMarkup(KeyboardContainer.getChannelsToSubscribeKeyboard(telegramChannelService));
        return message;
    }
}
