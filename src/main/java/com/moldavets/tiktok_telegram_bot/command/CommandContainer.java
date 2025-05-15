package com.moldavets.tiktok_telegram_bot.command;


import com.moldavets.tiktok_telegram_bot.command.Impl.StartCommand;

import java.util.Map;

public class CommandContainer {

    private final Map<String, Command> COMMAND_MAP;

    public CommandContainer() {
        this.COMMAND_MAP = Map.of(
                "/start", new StartCommand()
        );
    }

    public Command retrieveCommand(String command) {
        return COMMAND_MAP.getOrDefault(command, null);
    }
}
