package com.moldavets.tiktok_telegram_bot.command;

import com.moldavets.tiktok_telegram_bot.command.Impl.AdministratorCommand;
import com.moldavets.tiktok_telegram_bot.command.Impl.StartCommand;
import com.moldavets.tiktok_telegram_bot.command.Impl.UnknownCommand;
import com.moldavets.tiktok_telegram_bot.service.TelegramChannelService;
import com.moldavets.tiktok_telegram_bot.service.TelegramUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CommandContainerTest {

    @Mock
    TelegramUserService telegramUserService;

    @Mock
    TelegramChannelService telegramChannelService;

    Long ADMIN_ID = 123L;

    CommandContainer commandContainer;

    @BeforeEach
    void setUp() {
        commandContainer = new CommandContainer(telegramUserService, telegramChannelService, ADMIN_ID);
    }

    @Test
    void retrieveCommand_shouldReturnStartCommand_whenInputContainsValidData() {
        Command actual = commandContainer.retrieveCommand("/start");

        assertNotNull(actual);
        assertInstanceOf(StartCommand.class, actual);
    }

    @Test
    void retrieveCommand_shouldReturnAdministratorCommand_whenInputContainsValidData() {
        Command actual = commandContainer.retrieveCommand("/administrator");

        assertNotNull(actual);
        assertInstanceOf(AdministratorCommand.class, actual);
    }

    @Test
    void retrieveCommand_shouldReturnUnknownCommand_whenInputContainsInvalidData() {
        Command actual = commandContainer.retrieveCommand("/test");

        assertNotNull(actual);
        assertInstanceOf(UnknownCommand.class, actual);
    }


}