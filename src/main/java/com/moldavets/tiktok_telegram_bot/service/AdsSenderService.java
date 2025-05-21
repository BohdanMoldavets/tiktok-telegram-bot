package com.moldavets.tiktok_telegram_bot.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface AdsSenderService {

    SendMessage sendForwardMessageToAllUsers(Update update);

}
