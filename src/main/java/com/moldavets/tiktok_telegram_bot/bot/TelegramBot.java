package com.moldavets.tiktok_telegram_bot.bot;

import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public interface TelegramBot {

    void execute(SendMessage sendMessage) throws TelegramApiException;

    void executePhoto(SendPhoto sendPhoto) throws TelegramApiException;

    void executeVideo(SendVideo sendVideo) throws TelegramApiException;

    void execute(AnswerCallbackQuery sendVideo) throws TelegramApiException;

    void execute(DeleteMessage deleteMessage) throws TelegramApiException;

    ChatMember execute(GetChatMember getChatMember) throws TelegramApiException;

}
