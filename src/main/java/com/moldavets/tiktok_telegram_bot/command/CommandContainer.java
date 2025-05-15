package com.moldavets.tiktok_telegram_bot.command;


import com.moldavets.tiktok_telegram_bot.command.Impl.StartCommand;
import com.moldavets.tiktok_telegram_bot.command.Impl.TikTokLinkCommand;
import com.moldavets.tiktok_telegram_bot.service.TelegramChannelService;
import com.moldavets.tiktok_telegram_bot.service.TelegramUserService;

import java.util.Map;

public class CommandContainer {
    private final String TIKTOK_LINK_PATTERN = "https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)";

    private final Map<String, Command> COMMAND_MAP;

    public CommandContainer(TelegramUserService telegramUserService, TelegramChannelService telegramChannelService) {
        this.COMMAND_MAP = Map.of(
                "/start", new StartCommand(),
                "/tiktokcommand", new TikTokLinkCommand(telegramUserService, telegramChannelService)
        );
    }

    public Command retrieveCommand(String command) {
        if(command.matches(TIKTOK_LINK_PATTERN)) {
            return COMMAND_MAP.get("/tiktokcommand");//todo send TelegramUserService, SubscribeCheckerService() -> move to BotFacade
        }

        Command retrievedCommand = COMMAND_MAP.getOrDefault(command, null);
        return retrievedCommand;
//        return retrievedCommand == null ? new UnknownCommand() : retrievedCommand; //todo
    }
}
