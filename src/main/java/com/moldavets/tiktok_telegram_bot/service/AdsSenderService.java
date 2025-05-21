package com.moldavets.tiktok_telegram_bot.service;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface AdsSenderService {

    Integer sendForwardMessageToAllUsers(Update update);

}
