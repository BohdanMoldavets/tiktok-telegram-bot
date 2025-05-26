package com.moldavets.tiktok_telegram_bot.downloader.Impl;

import com.moldavets.tiktok_telegram_bot.bot.TelegramBot;
import com.moldavets.tiktok_telegram_bot.downloader.Downloader;
import com.moldavets.tiktok_telegram_bot.keyboard.KeyboardContainer;
import com.moldavets.tiktok_telegram_bot.logger.Impl.TelegramCustomLogger;
import com.moldavets.tiktok_telegram_bot.model.Impl.TelegramUser;
import com.moldavets.tiktok_telegram_bot.parser.Impl.TikTokParser;
import com.moldavets.tiktok_telegram_bot.parser.Impl.VideoParser;
import com.moldavets.tiktok_telegram_bot.service.TelegramChannelService;
import com.moldavets.tiktok_telegram_bot.service.TelegramUserService;
import com.moldavets.tiktok_telegram_bot.utils.MessageText;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;

public class TikTokDownloader implements Downloader {

    private TelegramBot telegramBot;

    private final TelegramUserService telegramUserService;
    private final TelegramChannelService telegramChannelService;

    public TikTokDownloader(TelegramUserService telegramUserService, TelegramChannelService telegramChannelService, TelegramBot telegramBot) {
        this.telegramUserService = telegramUserService;
        this.telegramChannelService = telegramChannelService;
        this.telegramBot = telegramBot;
    }

    @Override
    public BotApiMethod<?> execute(Update update) {
        Long userId = update.getMessage().getFrom().getId();
        String link = update.getMessage().getText();

        telegramUserService.checkTelegramUserRegistration(userId, update.getMessage().getFrom().getUserName());
        TelegramUser storedUser = telegramUserService.getById(userId);

        if (storedUser != null && storedUser.isSubscribed()) {
            try {
                Message waitMessage = telegramBot.executeAndReturn(new SendMessage(userId.toString(), "⌛"));

                SendVideo sendVideo = new SendVideo(userId.toString(), VideoParser.parse(TikTokParser.parse(link)));
                sendVideo.setCaption(MessageText.DOWNLOADER_ASSIGN_CAPTION.getMessageText());
                telegramBot.executeVideo(sendVideo);
                TelegramCustomLogger.getInstance().info(String.format("User [%s] just download the [%s]", userId, link));

                telegramBot.execute(new DeleteMessage(waitMessage.getChatId().toString(), waitMessage.getMessageId()));
                return this.createSendMessageBuilder(userId, MessageText.DOWNLOADER_NEXT_VIDEO_REQUEST.getMessageText()).build();
            } catch (TelegramApiException exception) {
                TelegramCustomLogger.getInstance().error(
                        String.format("Something went wrong while downloading tiktok for [%s] with link [%s] exception [%s]",
                                userId,
                                link,
                                exception
                        )
                );
                return this.createSendMessageBuilder(userId, MessageText.DOWNLOADER_SOMETHING_WENT_WRONG.getMessageText()).build();
            } catch (IOException ioe) {
                TelegramCustomLogger.getInstance().error(
                        String.format("Something went wrong while downloading tiktok for [%s] with link [%s] exception [%s]",
                                userId,
                                link,
                                ioe
                        )
                );
                return this.createSendMessageBuilder(userId, MessageText.DOWNLOADER_FAIL_WHILE_DOWNLOADING.getMessageText())
                        .disableWebPagePreview(true)
                        .build();
            }
        }

        return this.createSendMessageBuilder(userId, MessageText.DOWNLOADER_SUBSCRIPTION_REQUEST.getMessageText())
                .replyMarkup(KeyboardContainer.getChannelsToSubscribeKeyboard(telegramChannelService))
                .build();
    }

    private SendMessage.SendMessageBuilder createSendMessageBuilder(Long userId, String message) {
        return SendMessage.builder()
                .chatId(userId)
                .text(message)
                .parseMode("html");
    }
}
