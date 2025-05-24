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
                    || splitMessage[1].equals("users") || splitMessage[1].equals("groups")
                    || splitMessage[1].equals("ban") || splitMessage[1].equals("unban")) {
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

            case "ban":
                Long userIdWhoWillBeBanned = Long.parseLong(command[2]);
                telegramUserService.updateIsBannedById(userIdWhoWillBeBanned, true);
                return new SendMessage(
                        ADMIN_ID.toString(),
                        MessageText.COMMAND_ADMINISTRATOR_USER_BAN.getMessageText() + " - [" + userIdWhoWillBeBanned + "]"
                );

            case "unban":
                Long userIdWhoWillBeUnbanned = Long.parseLong(command[2]);
                telegramUserService.updateIsBannedById(userIdWhoWillBeUnbanned, false);
                return new SendMessage(
                        ADMIN_ID.toString(),
                        MessageText.COMMAND_ADMINISTRATOR_USER_UNBAN.getMessageText() + " - [" + userIdWhoWillBeUnbanned + "]"
                );

            case "groups":
                return new SendMessage(
                        ADMIN_ID.toString(),
                        getChannelsMessage()
                );

            case "users":
                return new SendMessage(
                        ADMIN_ID.toString(),
                        getUsersMessage()
                );
        }
        return null;
    }

    private String getUsersMessage() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append(MessageText.COMMAND_ADMINISTRATOR_ACTIVE_USERS_COUNT.getMessageText())
                .append(" - [")
                .append(telegramUserService.getAllActiveTelegramUsers().size())
                .append("]\n\n")
                .append(MessageText.COMMAND_ADMINISTRATOR_ALL_USERS_COUNT.getMessageText())
                .append(" - [")
                .append(telegramUserService.getAll().size())
                .append("]");
        return stringBuilder.toString();
    }

    private String getChannelsMessage() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("All available channels:\n");
        for (TelegramChannel channel : telegramChannelService.getAllWhereStatusIsActive()) {
            stringBuilder
                    .append("Id: ")
                    .append(channel.getId())
                    .append(" Link: ")
                    .append(channel.getChannelLink())
                    .append("\n");
        }
        return stringBuilder.toString();
    }
}
