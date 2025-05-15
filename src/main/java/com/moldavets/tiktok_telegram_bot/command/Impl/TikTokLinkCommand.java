package com.moldavets.tiktok_telegram_bot.command.Impl;

import com.moldavets.tiktok_telegram_bot.command.Command;
import com.moldavets.tiktok_telegram_bot.model.Impl.TelegramUser;
import com.moldavets.tiktok_telegram_bot.service.TelegramChannelService;
import com.moldavets.tiktok_telegram_bot.service.TelegramUserService;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

public class TikTokLinkCommand implements Command {

    private final TelegramUserService telegramUserService;
    private final TelegramChannelService telegramChannelService;

    public TikTokLinkCommand(TelegramUserService telegramUserService, TelegramChannelService telegramChannelService) {
        this.telegramUserService = telegramUserService;
        this.telegramChannelService = telegramChannelService;
    }

    @Override
    public BotApiMethod<?> execute(Update update) {
        Long userId = update.getMessage().getFrom().getId();
        TelegramUser storedUser = telegramUserService.getById(userId);

//        if(storedUser.getSubscribed()) {
            //return ttDownloadService = video
//        }

        SendMessage message = new SendMessage(userId.toString(), "unsubscribed");
        return message;


    }
}
