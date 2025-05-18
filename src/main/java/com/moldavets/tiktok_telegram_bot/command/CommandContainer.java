package com.moldavets.tiktok_telegram_bot.command;


import com.moldavets.tiktok_telegram_bot.command.Impl.AdministratorCommand;
import com.moldavets.tiktok_telegram_bot.command.Impl.StartCommand;
import com.moldavets.tiktok_telegram_bot.command.Impl.UnknownCommand;
import com.moldavets.tiktok_telegram_bot.service.TelegramChannelService;
import com.moldavets.tiktok_telegram_bot.service.TelegramUserService;

import java.util.Map;

public class CommandContainer {

    private final Map<String, Command> COMMAND_MAP;

    public CommandContainer(TelegramUserService telegramUserService, TelegramChannelService telegramChannelService, Long adminId) {
        this.COMMAND_MAP = Map.of(
                "/start", new StartCommand(telegramUserService),
                "/administrator", new AdministratorCommand(telegramUserService, telegramChannelService, adminId)
        );
    }

    public Command retrieveCommand(String command) {
        return COMMAND_MAP.getOrDefault(command, new UnknownCommand());
    }
}
