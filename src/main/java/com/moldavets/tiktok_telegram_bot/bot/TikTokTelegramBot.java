package com.moldavets.tiktok_telegram_bot.bot;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class TikTokTelegramBot extends TelegramLongPollingBot implements TelegramBot{

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
            execute(response);
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
    public void execute(AnswerCallbackQuery answerCallbackQuery) throws TelegramApiException {
        super.execute(answerCallbackQuery);
    }
}
