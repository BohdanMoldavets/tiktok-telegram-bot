package com.moldavets.tiktok_telegram_bot.command;


import com.moldavets.tiktok_telegram_bot.command.Impl.StartCommand;
import com.moldavets.tiktok_telegram_bot.command.Impl.TikTokLinkCommand;

import java.util.Map;

public class CommandContainer {
    private final String TIKTOK_LINK_PATTERN = "https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)";

    private final Map<String, Command> COMMAND_MAP;

    public CommandContainer() {
        this.COMMAND_MAP = Map.of(
                "/start", new StartCommand() //todo send TelegramUserService
        );
    }

    public Command retrieveCommand(String command) {
        if(command.matches(TIKTOK_LINK_PATTERN)) {
            return new TikTokLinkCommand(); //todo send TelegramUserService, SubscribeCheckerService() -> move to BotFacade
        }

        Command retrievedCommand = COMMAND_MAP.getOrDefault(command, null);
        return retrievedCommand;
//        return retrievedCommand == null ? new UnknownCommand() : retrievedCommand; //todo
    }
}
