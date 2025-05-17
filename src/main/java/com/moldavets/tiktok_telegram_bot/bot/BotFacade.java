package com.moldavets.tiktok_telegram_bot.bot;

import com.moldavets.tiktok_telegram_bot.callback.CallbackFacade;
import com.moldavets.tiktok_telegram_bot.command.CommandContainer;
import com.moldavets.tiktok_telegram_bot.downloader.DownloaderContainer;
import com.moldavets.tiktok_telegram_bot.service.TelegramChannelService;
import com.moldavets.tiktok_telegram_bot.service.TelegramUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class BotFacade {

    private final String COMMAND_PREFIX = "/";

    private final CommandContainer commandContainer;
    private final DownloaderContainer downloaderContainer;
    private final CallbackFacade callbackFacade;

    @Autowired
    public BotFacade(TelegramUserService telegramUserService, TelegramChannelService telegramChannelService, TelegramBot telegramBot) {
        this.commandContainer = new CommandContainer(telegramUserService, telegramChannelService);
        this.downloaderContainer = new DownloaderContainer(telegramUserService, telegramChannelService);
        this.callbackFacade = new CallbackFacade(telegramUserService, telegramChannelService, telegramBot);
    }

    public BotApiMethod<?> processUpdate(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        if(update.hasCallbackQuery()) {
            return callbackFacade.processCallback(callbackQuery.getData()).handle(callbackQuery);
        }

        if(update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText().trim();
            return message.startsWith(COMMAND_PREFIX) ?
                    commandContainer.retrieveCommand(message).execute(update) : downloaderContainer.processDownloader(message).execute(update);
        }
        return null; //todo delete null
    }
}
