package com.moldavets.tiktok_telegram_bot.bot;

import com.moldavets.tiktok_telegram_bot.callback.CallbackFacade;
import com.moldavets.tiktok_telegram_bot.command.CommandContainer;
import com.moldavets.tiktok_telegram_bot.downloader.DownloaderContainer;
import com.moldavets.tiktok_telegram_bot.logger.Impl.TelegramCustomLogger;
import com.moldavets.tiktok_telegram_bot.model.Impl.TelegramUser;
import com.moldavets.tiktok_telegram_bot.model.TelegramUserStatus;
import com.moldavets.tiktok_telegram_bot.service.AdsSenderService;
import com.moldavets.tiktok_telegram_bot.service.Impl.AdsSenderServiceImpl;
import com.moldavets.tiktok_telegram_bot.service.TelegramChannelService;
import com.moldavets.tiktok_telegram_bot.service.TelegramUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class BotFacade {

    private final String COMMAND_PREFIX = "/";

    private final Long ADS_CHANNEL_ID;

    private final CommandContainer commandContainer;
    private final DownloaderContainer downloaderContainer;
    private final CallbackFacade callbackFacade;

    private final TelegramUserService telegramUserService;
    private final AdsSenderService adsSenderService;

    @Autowired
    public BotFacade(TelegramUserService telegramUserService, TelegramChannelService telegramChannelService,
                     TelegramBot telegramBot, @Value("${telegram.bot.admin.id}") Long adminId,
                     @Value("${telegram.bot.ads.chat.id}") Long adsChatId,
                     @Value("${telegram.bot.log.chat.id}") Long logChatId) {
        this.commandContainer = new CommandContainer(telegramUserService, telegramChannelService, adminId);
        this.downloaderContainer = new DownloaderContainer(telegramUserService, telegramChannelService, telegramBot);
        this.callbackFacade = new CallbackFacade(telegramUserService, telegramChannelService, telegramBot);
        this.adsSenderService = new AdsSenderServiceImpl(telegramBot, telegramUserService, adminId);
        this.telegramUserService = telegramUserService;
        this.ADS_CHANNEL_ID = adsChatId;
        TelegramCustomLogger.init(telegramBot, logChatId);
        TelegramCustomLogger.getInstance().info("Telegram bot started");
    }

    public BotApiMethod<?> processUpdate(Update update) {
        if(update.hasMyChatMember()) {
            Long userId = update.getMyChatMember().getFrom().getId();
            String username = update.getMyChatMember().getFrom().getUserName();
            String status = update.getMyChatMember().getNewChatMember().getStatus().toUpperCase();

            telegramUserService.checkTelegramUserRegistration(userId, username);

            if(!status.equalsIgnoreCase(TelegramUserStatus.MEMBER.getStatusName())) {
                telegramUserService.updateStatusById(userId,
                        TelegramUserStatus.valueOf(status));
            }
        }

        if(update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            if(this.isTelegramUserBanned(callbackQuery.getFrom().getId())) {
                return null;
            }
            return callbackFacade.processCallback(callbackQuery.getData()).handle(callbackQuery);
        }

        if(update.hasChannelPost() && update.getChannelPost().getChatId().equals(ADS_CHANNEL_ID)) {
            return adsSenderService.sendForwardMessageToAllUsers(update);
        }

        if(update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText().trim();
            if(this.isTelegramUserBanned(update.getMessage().getFrom().getId())) {
                return null;
            }
            return message.startsWith(COMMAND_PREFIX) ?
                    commandContainer.retrieveCommand(message).execute(update) : downloaderContainer.processDownloader(message).execute(update);
        }

        return null;
    }


    public Boolean isTelegramUserBanned(Long userId) {
        TelegramUser storedTelegramUser = telegramUserService.getById(userId);
        return storedTelegramUser != null && storedTelegramUser.isBanned();
    }
}
