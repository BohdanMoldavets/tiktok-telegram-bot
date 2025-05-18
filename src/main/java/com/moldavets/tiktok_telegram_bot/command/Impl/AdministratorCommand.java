package com.moldavets.tiktok_telegram_bot.command.Impl;

import com.moldavets.tiktok_telegram_bot.command.Command;
import com.moldavets.tiktok_telegram_bot.service.TelegramChannelService;
import com.moldavets.tiktok_telegram_bot.service.TelegramUserService;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class AdministratorCommand implements Command {

    private final Long ADMIN_ID;
    private final TelegramUserService telegramUserService;
    private final TelegramChannelService telegramChannelService;

    public AdministratorCommand(TelegramUserService telegramUserService,
                                TelegramChannelService telegramChannelService, Long adminId) {
        this.telegramUserService = telegramUserService;
        this.telegramChannelService = telegramChannelService;
        this.ADMIN_ID = adminId;
    }

    @Override
    public BotApiMethod<?> execute(Update update) {
        Long userId = update.getMessage().getFrom().getId();
        if(userId.equals(ADMIN_ID)) {
            return new SendMessage(userId.toString(), "Administrator Command");
        } else {
            return new UnknownCommand().execute(update);
        }
    }
}
