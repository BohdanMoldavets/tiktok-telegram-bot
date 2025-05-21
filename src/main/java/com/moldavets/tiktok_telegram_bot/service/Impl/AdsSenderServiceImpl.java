package com.moldavets.tiktok_telegram_bot.service.Impl;

import com.moldavets.tiktok_telegram_bot.bot.TelegramBot;
import com.moldavets.tiktok_telegram_bot.model.Impl.TelegramUser;
import com.moldavets.tiktok_telegram_bot.service.AdsSenderService;
import com.moldavets.tiktok_telegram_bot.service.TelegramUserService;
import com.moldavets.tiktok_telegram_bot.utils.MessageText;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Set;

public class AdsSenderServiceImpl implements AdsSenderService {

    private final Long ADMIN_ID;

    private final TelegramBot telegramBot;
    private final TelegramUserService telegramUserService;

    public AdsSenderServiceImpl(TelegramBot telegramBot,
                                TelegramUserService telegramUserService, Long adminId) {
        this.telegramBot = telegramBot;
        this.telegramUserService = telegramUserService;
        this.ADMIN_ID = adminId;
    }

    @Override
    public SendMessage sendForwardMessageToAllUsers(Update update) {
        Message message = update.getChannelPost();
        Set<TelegramUser> allActiveUsersSet = telegramUserService.getAllActiveTelegramUsers();

        try {
            if(message.hasVideo()) {
                handleVideoAd(allActiveUsersSet, update);
            } else if(message.hasPhoto()) {
                handlePhotoAd(allActiveUsersSet, update);
            } else if(message.hasText()) {
                handleTextAd(allActiveUsersSet, update);
            }
        } catch (TelegramApiException e) {
            throw new RuntimeException(e); //todo delete
        }

        return new SendMessage(
                ADMIN_ID.toString(),
                MessageText.ADS_AMOUNT_OF_USERS_WHO_RECEIVED_AD.getMessageText() + " - " + allActiveUsersSet.size()
        );
    }

    private void handleTextAd(Set<TelegramUser> telegramUsers, Update update) throws TelegramApiException {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(update.getChannelPost().getText());
        sendMessage.setReplyMarkup(update.getChannelPost().getReplyMarkup());
        for (TelegramUser telegramUser : telegramUsers) {
            sendMessage.setChatId(telegramUser.getId());
            telegramBot.execute(sendMessage);
        }
    }

    private void handlePhotoAd(Set<TelegramUser> telegramUsers, Update update) throws TelegramApiException {
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setPhoto(new InputFile(update.getChannelPost().getPhoto().getLast().getFileId()));
        sendPhoto.setCaption(update.getChannelPost().getCaption());
        sendPhoto.setReplyMarkup(update.getChannelPost().getReplyMarkup());
        for (TelegramUser telegramUser : telegramUsers) {
            sendPhoto.setChatId(telegramUser.getId());
            telegramBot.executePhoto(sendPhoto);
        }
    }

    private void handleVideoAd(Set<TelegramUser> telegramUsers, Update update) throws TelegramApiException {
        SendVideo sendVideo = new SendVideo();
        sendVideo.setVideo(new InputFile(update.getChannelPost().getVideo().getFileId()));
        sendVideo.setCaption(update.getChannelPost().getCaption());
        sendVideo.setReplyMarkup(update.getChannelPost().getReplyMarkup());
        for (TelegramUser telegramUser : telegramUsers) {
            sendVideo.setChatId(telegramUser.getId());
            telegramBot.executeVideo(sendVideo);
        }
    }
}
