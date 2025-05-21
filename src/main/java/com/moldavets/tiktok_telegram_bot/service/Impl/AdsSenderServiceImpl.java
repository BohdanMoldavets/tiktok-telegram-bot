package com.moldavets.tiktok_telegram_bot.service.Impl;

import com.moldavets.tiktok_telegram_bot.bot.TelegramBot;
import com.moldavets.tiktok_telegram_bot.model.Impl.TelegramUser;
import com.moldavets.tiktok_telegram_bot.service.AdsSenderService;
import com.moldavets.tiktok_telegram_bot.service.TelegramUserService;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Set;

public class AdsSenderServiceImpl implements AdsSenderService {

    private final TelegramBot telegramBot;
    private final TelegramUserService telegramUserService;

    public AdsSenderServiceImpl(TelegramBot telegramBot,
                                TelegramUserService telegramUserService) {
        this.telegramBot = telegramBot;
        this.telegramUserService = telegramUserService;
    }

    @Override
    public Integer sendForwardMessageToAllUsers(Update update) {
        Message message = update.getChannelPost();

        Set<TelegramUser> allActiveUsersSet = telegramUserService.getAllActiveTelegramUsers();

        try {
            if(message.hasVideo()) {
                SendVideo sendVideo = handleVideoAd(allActiveUsersSet, update);
            } else if(message.hasPhoto()) {
                SendPhoto sendPhoto = handlePhotoAd(allActiveUsersSet, update);
            } else if(message.hasText()) {
                SendMessage sendMessage = handleTextAd(allActiveUsersSet, update);
            }
        } catch (TelegramApiException e) {
            throw new RuntimeException(e); //todo delete
        }

        return allActiveUsersSet.size();
    }

    private SendMessage handleTextAd(Set<TelegramUser> telegramUsers, Update update) throws TelegramApiException {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(update.getChannelPost().getText());
        sendMessage.setReplyMarkup(update.getChannelPost().getReplyMarkup());
        for (TelegramUser telegramUser : telegramUsers) {
            sendMessage.setChatId(telegramUser.getId());
            telegramBot.execute(sendMessage);
        }
        return null;
    }

    private SendPhoto handlePhotoAd(Set<TelegramUser> telegramUsers, Update update) throws TelegramApiException {
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setPhoto(new InputFile(update.getMessage().getPhoto().getLast().getFileId()));
        sendPhoto.setCaption(update.getMessage().getCaption());
        sendPhoto.setReplyMarkup(update.getMessage().getReplyMarkup());
        return sendPhoto;
    }

    private SendVideo handleVideoAd(Set<TelegramUser> telegramUsers, Update update) throws TelegramApiException {
        SendVideo sendVideo = new SendVideo();
        sendVideo.setVideo(new InputFile(update.getMessage().getVideo().getFileId()));
        sendVideo.setCaption(update.getMessage().getCaption());
        sendVideo.setReplyMarkup(update.getMessage().getReplyMarkup());
        return sendVideo;
    }
}
