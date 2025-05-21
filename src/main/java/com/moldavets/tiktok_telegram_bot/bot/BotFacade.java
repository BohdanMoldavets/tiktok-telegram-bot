package com.moldavets.tiktok_telegram_bot.bot;

import com.moldavets.tiktok_telegram_bot.callback.CallbackFacade;
import com.moldavets.tiktok_telegram_bot.command.CommandContainer;
import com.moldavets.tiktok_telegram_bot.downloader.DownloaderContainer;
import com.moldavets.tiktok_telegram_bot.model.TelegramUserStatus;
import com.moldavets.tiktok_telegram_bot.service.AdsSenderService;
import com.moldavets.tiktok_telegram_bot.service.Impl.AdsSenderServiceImpl;
import com.moldavets.tiktok_telegram_bot.service.TelegramChannelService;
import com.moldavets.tiktok_telegram_bot.service.TelegramUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Component
public class BotFacade {

    private final String COMMAND_PREFIX = "/";

    private final Long ADMIN_ID;
    private final Long ADS_CHANNEL_ID;

    private final CommandContainer commandContainer;
    private final DownloaderContainer downloaderContainer;
    private final CallbackFacade callbackFacade;

    private final TelegramUserService telegramUserService;
    private final AdsSenderService adsSenderService;

    @Autowired
    public BotFacade(TelegramUserService telegramUserService, TelegramChannelService telegramChannelService,
                     TelegramBot telegramBot, @Value("${telegram.bot.admin.id}") Long adminId,
                     @Value("${telegram.bot.ads.chat.id}") Long adsChatId) {
        this.commandContainer = new CommandContainer(telegramUserService, telegramChannelService, adminId);
        this.downloaderContainer = new DownloaderContainer(telegramUserService, telegramChannelService, telegramBot);
        this.callbackFacade = new CallbackFacade(telegramUserService, telegramChannelService, telegramBot);
        this.adsSenderService = new AdsSenderServiceImpl(telegramBot, telegramUserService);
        this.telegramUserService = telegramUserService;
        this.ADMIN_ID = adminId;
        this.ADS_CHANNEL_ID = adsChatId;
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

//        if(update.hasMessage() //todo delete
//                && (update.getMessage().getForwardFrom() != null || update.getMessage().getForwardFromChat() != null)
//                && update.getMessage().getFrom().getId().equals(ADMIN_ID)) {
//            return new SendMessage(ADMIN_ID.toString(), adsSenderService.sendForwardMessageToAllUsers(update).toString());
//        }

        if(update.hasChannelPost() && update.getChannelPost().getChatId().equals(ADS_CHANNEL_ID)) {
            return new SendMessage(ADMIN_ID.toString(), adsSenderService.sendForwardMessageToAllUsers(update).toString());
        }

        if(update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText().trim();
            return message.startsWith(COMMAND_PREFIX) ?
                    commandContainer.retrieveCommand(message).execute(update) : downloaderContainer.processDownloader(message).execute(update);
        }

        return null;
    }
}
