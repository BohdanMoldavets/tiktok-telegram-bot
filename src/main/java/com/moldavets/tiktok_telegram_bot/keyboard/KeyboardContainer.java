package com.moldavets.tiktok_telegram_bot.keyboard;

import com.moldavets.tiktok_telegram_bot.model.Impl.TelegramChannel;
import com.moldavets.tiktok_telegram_bot.service.TelegramChannelService;
import com.moldavets.tiktok_telegram_bot.utils.MessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public final class KeyboardContainer {

    private KeyboardContainer() {
    }

    public static InlineKeyboardMarkup getChannelsToSubscribeKeyboard(TelegramChannelService telegramChannelService) {

        List<List<InlineKeyboardButton>> resultKeyboard = new ArrayList<>();

        for(TelegramChannel channel : telegramChannelService.getAllWhereStatusIsActive()) {
            resultKeyboard.add(
                List.of(
                        new InlineKeyboardButton(
                                MessageText.KEYBOARD_BUTTON_SUBSCRIPTION_REQUEST.getMessageText(),
                                channel.getChannelLink(),
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null
                        )
                )
            );
        }

        resultKeyboard.add(
            List.of(
                    new InlineKeyboardButton(
                            MessageText.KEYBOARD_BUTTON_CHECK_SUBSCRIPTION.getMessageText(),
                            null,
                            "CHECK_SUBSCRIPTION_ON_REQUIRED_CHANNELS",
                            null,
                            null,
                            null,
                            null,
                            null,
                            null
                    )
            )
        );

        return new InlineKeyboardMarkup(resultKeyboard);
    }
}
