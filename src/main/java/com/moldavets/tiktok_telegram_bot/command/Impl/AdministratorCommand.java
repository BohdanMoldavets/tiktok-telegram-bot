package com.moldavets.tiktok_telegram_bot.command.Impl;

import com.moldavets.tiktok_telegram_bot.command.Command;
import com.moldavets.tiktok_telegram_bot.model.Impl.TelegramChannel;
import com.moldavets.tiktok_telegram_bot.model.TelegramChannelStatus;
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
        if (userId.equals(ADMIN_ID)) {
            System.out.println("123");
            String[] splitMessage = update.getMessage().getText().trim().split(" ");

            if(splitMessage[1].equals("list")) {
                return new SendMessage(userId.toString(), telegramChannelService.getAllWhereStatusIsActive().toString());
            } else if(splitMessage[1].equals("add") || splitMessage[1].equals("delete")) {
                return processCommand(splitMessage);
            }

        }
        return new UnknownCommand().execute(update);
    }

    private SendMessage processCommand(String[] command) {
        switch (command[1]) {
            case "add":
                telegramChannelService.save(new TelegramChannel(Long.parseLong(command[2]), command[3], TelegramChannelStatus.ACTIVE));
                break;
            case "delete":
                telegramChannelService.updateStatusById(Long.parseLong(command[2]), TelegramChannelStatus.INACTIVE);
                break;
        }
        return new SendMessage(ADMIN_ID.toString(), telegramChannelService.getAllWhereStatusIsActive().toString());
    }
}
