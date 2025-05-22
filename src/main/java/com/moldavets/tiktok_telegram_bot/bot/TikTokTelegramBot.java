package com.moldavets.tiktok_telegram_bot.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class TikTokTelegramBot extends TelegramLongPollingBot implements TelegramBot {

    @Lazy
    @Autowired
    private BotFacade botFacade;

    @Value("${bot.name}")
    String botName;

    @Value("${bot.token}")
    String botToken;

    @Override
    public void onUpdateReceived(Update update) {
        BotApiMethod<?> response = botFacade.processUpdate(update);
        try {
            if(response != null) {
                execute(response);
            }
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void execute(SendMessage sendMessage) throws TelegramApiException {
        super.execute(sendMessage);
    }

    @Override
    public Message executeAndReturn(SendMessage sendMessage) throws TelegramApiException {
//        sendMessage.enableHtml(true);
        return super.execute(sendMessage);
    }

    @Override
    public void executePhoto(SendPhoto sendPhoto) throws TelegramApiException {
        super.execute(sendPhoto);
    }

    @Override
    public void executeVideo(SendVideo sendVideo) throws TelegramApiException {
        super.execute(sendVideo);
    }

    @Override
    public void execute(AnswerCallbackQuery answerCallbackQuery) throws TelegramApiException {
        super.execute(answerCallbackQuery);
    }

    @Override
    public void execute(DeleteMessage deleteMessage) throws TelegramApiException {
        super.execute(deleteMessage);
    }

    @Override
    public ChatMember execute(GetChatMember getChatMember) throws TelegramApiException {
        return super.execute(getChatMember);
    }
}
