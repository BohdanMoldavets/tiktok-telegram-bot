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

        List<TelegramChannel> channels = new ArrayList<>();

        return InlineKeyboardMarkup.builder().keyboard(
                List.of(
                        List.of(
                                new InlineKeyboardButton("Group1", "https://t.me/+qRyA29kZozhiY2U0", null, null, null, null, null, null, null)
                        ),
                        List.of(
                                new InlineKeyboardButton("Group2", "https://t.me/+qRyA29kZozhiY2U0", null, null, null, null, null, null, null)

                        )
                )
        ).build();
    }
}
