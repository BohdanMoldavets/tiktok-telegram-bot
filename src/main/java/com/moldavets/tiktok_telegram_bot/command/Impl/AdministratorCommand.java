package com.moldavets.tiktok_telegram_bot.command.Impl;

import com.moldavets.tiktok_telegram_bot.command.Command;
import com.moldavets.tiktok_telegram_bot.model.Impl.TelegramChannel;
import com.moldavets.tiktok_telegram_bot.model.TelegramChannelStatus;
import com.moldavets.tiktok_telegram_bot.service.TelegramChannelService;
import com.moldavets.tiktok_telegram_bot.service.TelegramUserService;
import com.moldavets.tiktok_telegram_bot.utils.MessageText;
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
        SendMessage sendMessage = null;

        if (userId.equals(ADMIN_ID) && splitMessage.length > 1) {

            if(splitMessage[1].equals("add") || splitMessage[1].equals("delete")
                    || splitMessage[1].equals("list") || splitMessage[1].equals("groups")) {
                sendMessage = processCommand(splitMessage);
            }

        }
        return sendMessage != null ? sendMessage : new UnknownCommand().execute(update);
    }


    private SendMessage processCommand(String[] command) {
        switch (command[1]) {
            case "add":
                Long channelIdToAdd = Long.parseLong(command[2]);

                telegramChannelService.saveOrUpdate(
                        new TelegramChannel(channelIdToAdd, command[3],
                        TelegramChannelStatus.ACTIVE)
                );

                telegramUserService.updateSubscriptionForAllUsers(false);

                return new SendMessage(
                        ADMIN_ID.toString(),
                        MessageText.COMMAND_ADMINISTRATOR_CHANNEL_ADDED.getMessageText() + " - [" + channelIdToAdd + "]"
                );

            case "delete":
                Long channelIdToDelete = Long.parseLong(command[2]);
                telegramChannelService.updateStatusById(channelIdToDelete, TelegramChannelStatus.INACTIVE);

                return new SendMessage(
                        ADMIN_ID.toString(),
                        MessageText.COMMAND_ADMINISTRATOR_CHANNEL_DELETED.getMessageText() + " - [" + channelIdToDelete + "]"
                );

            case "groups":
                return new SendMessage(
                        ADMIN_ID.toString(),
                        formatChannelsMessage(telegramChannelService.getAllWhereStatusIsActive())
                );

                case "list":
                return new SendMessage(
                    ADMIN_ID.toString(),
                    MessageText.COMMAND_ADMINISTRATOR_USERS_COUNT.getMessageText() + " - " + telegramUserService.getAllActiveTelegramUsers().size()
            );
        }
        return null;
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
