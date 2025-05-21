package com.moldavets.tiktok_telegram_bot.service.Impl;

import com.moldavets.tiktok_telegram_bot.bot.TelegramBot;
import com.moldavets.tiktok_telegram_bot.model.Impl.TelegramUser;
import com.moldavets.tiktok_telegram_bot.service.AdsSenderService;
import com.moldavets.tiktok_telegram_bot.service.TelegramChannelService;
import com.moldavets.tiktok_telegram_bot.service.TelegramUserService;
import org.telegram.telegrambots.meta.api.objects.Update;

public class AdsSenderServiceImpl implements AdsSenderService {

    private final TelegramBot telegramBot;
    private final TelegramUserService telegramUserService;
    private final TelegramChannelService telegramChannelService;

    public AdsSenderServiceImpl(TelegramBot telegramBot,
                                TelegramUserService telegramUserService,
                                TelegramChannelService telegramChannelService) {
        this.telegramBot = telegramBot;
        this.telegramUserService = telegramUserService;
        this.telegramChannelService = telegramChannelService;
    }

    @Override
    public Integer sendForwardMessageToAllUsers(Update update) {
//        Set<TelegramUser> allActiveUsersSet
        return null;

    }

}
