package com.moldavets.tiktok_telegram_bot.callback;

import com.moldavets.tiktok_telegram_bot.bot.TelegramBot;
import com.moldavets.tiktok_telegram_bot.callback.Impl.SubscriptionCheckerCallbackHandler;
import com.moldavets.tiktok_telegram_bot.service.TelegramChannelService;
import com.moldavets.tiktok_telegram_bot.service.TelegramUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CallbackFacadeTest {

    @Mock
    TelegramUserService telegramUserService;

    @Mock
    TelegramChannelService telegramChannelService;

    @Mock
    TelegramBot telegramBot;

    CallbackFacade callbackFacade;

    @BeforeEach
    void setUp() {
        callbackFacade = new CallbackFacade(telegramUserService, telegramChannelService, telegramBot);
    }

    @Test
    void processCallback_shouldReturnSubscriptionCheckerCallbackHandler_whenInputDataIsValid() {
        CallbackHandler actual = callbackFacade
                .processCallback("CHECK_SUBSCRIPTION_ON_REQUIRED_CHANNELS");

        assertNotNull(actual);
        assertInstanceOf(SubscriptionCheckerCallbackHandler.class, actual);
    }

    @Test
    void processCallback_shouldReturnNull_whenInputDataInvalid() {
        CallbackHandler actual = callbackFacade
                .processCallback("CHECK_SOMETHING_FOR_TESTING");

        assertNull(actual);
    }
}