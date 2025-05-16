package com.moldavets.tiktok_telegram_bot.downloader.Impl;

import com.moldavets.tiktok_telegram_bot.downloader.Downloader;
import com.moldavets.tiktok_telegram_bot.keyboard.KeyboardContainer;
import com.moldavets.tiktok_telegram_bot.model.Impl.TelegramUser;
import com.moldavets.tiktok_telegram_bot.service.TelegramChannelService;
import com.moldavets.tiktok_telegram_bot.service.TelegramUserService;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

public class TikTokDownloader implements Downloader {

    private final String MESSAGE = "Please subscribe on this groups for Download TikTok";

    private final TelegramUserService telegramUserService;
    private final TelegramChannelService telegramChannelService;

    public TikTokDownloader(TelegramUserService telegramUserService, TelegramChannelService telegramChannelService) {
        this.telegramUserService = telegramUserService;
        this.telegramChannelService = telegramChannelService;
    }

    @Override
    public BotApiMethod<?> execute(Update update) {
        Long userId = update.getMessage().getFrom().getId();
        String username = update.getMessage().getFrom().getUserName();
        telegramUserService.checkTelegramUserRegistration(userId, username);

        TelegramUser storedUser = telegramUserService.getById(userId);
        if (storedUser != null && storedUser.isSubscribed()) {
            //"redirect" to download video
        }

        SendMessage message = new SendMessage(userId.toString(), MESSAGE);
        message.setReplyMarkup(KeyboardContainer.getChannelsToSubscribeKeyboard(telegramChannelService));
        return message;
    }
}
