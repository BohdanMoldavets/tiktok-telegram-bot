package com.moldavets.tiktok_telegram_bot.service.Impl;

import com.moldavets.tiktok_telegram_bot.bot.TelegramBot;
import com.moldavets.tiktok_telegram_bot.logger.Impl.TelegramCustomLogger;
import com.moldavets.tiktok_telegram_bot.model.Impl.TelegramUser;
import com.moldavets.tiktok_telegram_bot.model.TelegramUserStatus;
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
        Integer resultCountUsersWhichReceiveAd = 0;
        Message message = update.getChannelPost();
        Set<TelegramUser> allActiveUsersSet = telegramUserService.getAllActiveTelegramUsers();

        if(message.hasVideo()) {
            resultCountUsersWhichReceiveAd = handleVideoAd(allActiveUsersSet, update);
        } else if(message.hasPhoto()) {
            resultCountUsersWhichReceiveAd = handlePhotoAd(allActiveUsersSet, update);
        } else if(message.hasText()) {
            resultCountUsersWhichReceiveAd = handleTextAd(allActiveUsersSet, update);
        }


        TelegramCustomLogger.getInstance().info(
                MessageText.ADS_AMOUNT_OF_USERS_WHO_RECEIVED_AD.getMessageText() + " - " + resultCountUsersWhichReceiveAd
        );
        return new SendMessage(
                ADMIN_ID.toString(),
                MessageText.ADS_AMOUNT_OF_USERS_WHO_RECEIVED_AD.getMessageText() + " - " + resultCountUsersWhichReceiveAd
        );
    }

    private Integer handleTextAd(Set<TelegramUser> telegramUsers, Update update) {
        Integer countUsersWhichReceiveAd = 0;

        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(update.getChannelPost().getText());
        sendMessage.setReplyMarkup(update.getChannelPost().getReplyMarkup());

        for (TelegramUser telegramUser : telegramUsers) {
            Long userId = telegramUser.getId();
            sendMessage.setChatId(userId);

            try {
                telegramBot.execute(sendMessage);
                countUsersWhichReceiveAd++;
            } catch (TelegramApiException e) {
                TelegramCustomLogger.getInstance().error(
                        String.format("Something went wrong while send text ad to user [%s] exception [%s]", userId, e));
                telegramUserService.updateStatusById(userId, TelegramUserStatus.KICKED);
            }
        }
        return countUsersWhichReceiveAd;
    }

    private Integer handlePhotoAd(Set<TelegramUser> telegramUsers, Update update) {
        Integer countUsersWhichReceiveAd = 0;

        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setPhoto(new InputFile(update.getChannelPost().getPhoto().getLast().getFileId()));
        sendPhoto.setCaption(update.getChannelPost().getCaption());
        sendPhoto.setReplyMarkup(update.getChannelPost().getReplyMarkup());

        for (TelegramUser telegramUser : telegramUsers) {
            Long userId = telegramUser.getId();
            sendPhoto.setChatId(userId);

            try {
                telegramBot.executePhoto(sendPhoto);
                countUsersWhichReceiveAd++;
            } catch (TelegramApiException e) {
                TelegramCustomLogger.getInstance().error(
                        String.format("Something went wrong while send photo ad to user [%s] exception [%s]", userId, e));
                telegramUserService.updateStatusById(userId, TelegramUserStatus.KICKED);
            }
        }
        return countUsersWhichReceiveAd;
    }

    private Integer handleVideoAd(Set<TelegramUser> telegramUsers, Update update) {
        Integer countUsersWhichReceiveAd = 0;

        SendVideo sendVideo = new SendVideo();
        sendVideo.setVideo(new InputFile(update.getChannelPost().getVideo().getFileId()));
        sendVideo.setCaption(update.getChannelPost().getCaption());
        sendVideo.setReplyMarkup(update.getChannelPost().getReplyMarkup());

        for (TelegramUser telegramUser : telegramUsers) {
            Long userId = telegramUser.getId();
            sendVideo.setChatId(userId);

            try {
                telegramBot.executeVideo(sendVideo);
                countUsersWhichReceiveAd++;
            } catch (TelegramApiException e) {
                TelegramCustomLogger.getInstance().error(
                        String.format("Something went wrong while send video ad to user [%s] exception [%s]", userId, e));
                telegramUserService.updateStatusById(userId, TelegramUserStatus.KICKED);
            }
        }
        return countUsersWhichReceiveAd;
    }
}
