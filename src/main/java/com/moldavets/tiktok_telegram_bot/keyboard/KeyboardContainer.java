package com.moldavets.tiktok_telegram_bot.keyboard;

import com.moldavets.tiktok_telegram_bot.model.Impl.TelegramChannel;
import com.moldavets.tiktok_telegram_bot.service.TelegramChannelService;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public final class KeyboardContainer {

    private KeyboardContainer() {
    }

    //todo factory

    public static InlineKeyboardMarkup getChannelsToSubscribeKeyboard(TelegramChannelService telegramChannelService) {

        List<List<InlineKeyboardButton>> resultKeyboard = new ArrayList<>();

        for(TelegramChannel channel : telegramChannelService.getAllWhereStatusIsActive()) {
            resultKeyboard.add(
                List.of(
                        new InlineKeyboardButton("Subscribe to this channel ✅", channel.getChannelLink(), null, null, null, null, null, null, null)
                )
            );
        }

        resultKeyboard.add(
            List.of(
                    new InlineKeyboardButton("Check subscription \uD83D\uDD04", null, "SUBSCRIBED_ON_REQUIRED_CHANNELS", null, null, null, null, null, null)
            )
        );

        return new InlineKeyboardMarkup(resultKeyboard);
    }
}
