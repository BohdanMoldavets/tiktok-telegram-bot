package com.moldavets.tiktok_telegram_bot.bot;

import com.moldavets.tiktok_telegram_bot.callback.CallbackFacade;
import com.moldavets.tiktok_telegram_bot.command.CommandContainer;
import com.moldavets.tiktok_telegram_bot.downloader.DownloaderContainer;
import com.moldavets.tiktok_telegram_bot.model.TelegramUserStatus;
import com.moldavets.tiktok_telegram_bot.service.TelegramChannelService;
import com.moldavets.tiktok_telegram_bot.service.TelegramUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updates.Close;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Component
public class BotFacade {

    private final String COMMAND_PREFIX = "/";

    private final CommandContainer commandContainer;
    private final DownloaderContainer downloaderContainer;
    private final CallbackFacade callbackFacade;

    private final TelegramUserService telegramUserService;

    @Autowired
    public BotFacade(TelegramUserService telegramUserService, TelegramChannelService telegramChannelService, TelegramBot telegramBot) {
        this.commandContainer = new CommandContainer(telegramUserService, telegramChannelService);
        this.downloaderContainer = new DownloaderContainer(telegramUserService, telegramChannelService, telegramBot);
        this.callbackFacade = new CallbackFacade(telegramUserService, telegramChannelService, telegramBot);
        this.telegramUserService = telegramUserService;
    }

    public BotApiMethod<?> processUpdate(Update update) {
        if(update.hasMyChatMember()) {
            Long userId = update.getMyChatMember().getFrom().getId();
            String username = update.getMyChatMember().getFrom().getUserName();
            telegramUserService.checkTelegramUserRegistration(userId, username);
            telegramUserService.updateStatusById(userId,
                    TelegramUserStatus.valueOf(update.getMyChatMember().getNewChatMember().getStatus().toUpperCase()));
        }

        if(update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            return callbackFacade.processCallback(callbackQuery.getData()).handle(callbackQuery);
        }

        if(update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText().trim();
            return message.startsWith(COMMAND_PREFIX) ?
                    commandContainer.retrieveCommand(message).execute(update) : downloaderContainer.processDownloader(message).execute(update);
        }
        return null;
    }
}
