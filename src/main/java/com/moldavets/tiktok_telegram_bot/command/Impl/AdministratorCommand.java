package com.moldavets.tiktok_telegram_bot.command.Impl;

import com.moldavets.tiktok_telegram_bot.command.Command;
import com.moldavets.tiktok_telegram_bot.model.Impl.TelegramChannel;
import com.moldavets.tiktok_telegram_bot.model.TelegramChannelStatus;
import com.moldavets.tiktok_telegram_bot.service.TelegramChannelService;
import com.moldavets.tiktok_telegram_bot.service.TelegramUserService;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

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
        String[] splitMessage = update.getMessage().getText().trim().split(" ");
        if (userId.equals(ADMIN_ID) && splitMessage.length > 1) {

            if(splitMessage[1].equals("list")) {
                return new SendMessage(ADMIN_ID.toString(), formatChannelsMessage(telegramChannelService.getAllWhereStatusIsActive()));
            } else if(splitMessage[1].equals("add") || splitMessage[1].equals("delete")) {
                return processCommand(splitMessage);
            }
        }
        return new UnknownCommand().execute(update);
    }


    private SendMessage processCommand(String[] command) {
        switch (command[1]) {
            case "add":
                telegramChannelService.saveOrUpdate(new TelegramChannel(Long.parseLong(command[2]), command[3], TelegramChannelStatus.ACTIVE));
                telegramUserService.updateSubscriptionForAllUsers(false);
                break;
            case "delete":
                telegramChannelService.updateStatusById(Long.parseLong(command[2]), TelegramChannelStatus.INACTIVE);
                break;
        }
        return new SendMessage(ADMIN_ID.toString(), formatChannelsMessage(telegramChannelService.getAllWhereStatusIsActive()));
    }

    private String formatChannelsMessage(List<TelegramChannel> channels) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("All available channels:\n");
        for (TelegramChannel channel : channels) {
            stringBuilder
                    .append("Id: ")
                    .append(channel.getId())
                    .append(" Link: ")
                    .append(channel.getChannelLink());
        }
        return stringBuilder.toString();
    }
}
