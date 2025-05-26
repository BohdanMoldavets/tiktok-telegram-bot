package com.moldavets.tiktok_telegram_bot.command.Impl;

import com.moldavets.tiktok_telegram_bot.logger.Impl.TelegramCustomLogger;
import com.moldavets.tiktok_telegram_bot.model.Impl.TelegramChannel;
import com.moldavets.tiktok_telegram_bot.model.Impl.TelegramUser;
import com.moldavets.tiktok_telegram_bot.model.TelegramChannelStatus;
import com.moldavets.tiktok_telegram_bot.service.TelegramChannelService;
import com.moldavets.tiktok_telegram_bot.service.TelegramUserService;
import com.moldavets.tiktok_telegram_bot.utils.MessageText;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
class AdministratorCommandTest {

    @Mock
    TelegramUserService telegramUserService;

    @Mock
    TelegramChannelService telegramChannelService;


    AdministratorCommand administratorCommand;

    private Long ADMIN_ID = 123L;
    private TelegramUser telegramUser;
    private Chat chat;
    private Message waitMessage;
    private Message message;
    private Update update;
    private User user;

    @BeforeEach
    void setUp() {
        administratorCommand = new AdministratorCommand(telegramUserService, telegramChannelService, ADMIN_ID);

        telegramUser = new TelegramUser(ADMIN_ID, "tester", "member", true);

        chat = new Chat();
        chat.setId(telegramUser.getId());

        waitMessage = new Message();
        waitMessage.setChat(chat);
        waitMessage.setMessageId(321);

        update = new Update();
        message = new Message();
        user = new User();

        user.setId(telegramUser.getId());
        user.setUserName("tester");
        message.setFrom(user);
        update.setMessage(message);
    }

    @Test
    void execute_shouldAddNewChannelAndSetFalseSubscribtionForAllUsersAndReturnSendMessage_whenInputContainsAddParamAndChannelIdAndChannelLink() {
        message.setText("/administrator add -321 https://example.com/TEST");

        Mockito.doNothing().when(telegramChannelService).saveOrUpdate(any(TelegramChannel.class));
        Mockito.doNothing().when(telegramUserService).updateSubscriptionForAllUsers(anyBoolean());

        SendMessage actual = (SendMessage) administratorCommand.execute(update);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(ADMIN_ID.toString(), actual.getChatId());
        Assertions.assertEquals(MessageText.COMMAND_ADMINISTRATOR_CHANNEL_ADDED.getMessageText() + " - [-321]", actual.getText());

        Mockito.verify(telegramChannelService, Mockito.times(1)).saveOrUpdate(any(TelegramChannel.class));
        Mockito.verify(telegramUserService, Mockito.times(1)).updateSubscriptionForAllUsers(anyBoolean());
    }

    @Test
    void execute_shouldChangeChannelStatusToInactive_whenInputContainsDeleteParamAndChannelId() {
        message.setText("/administrator delete -321");

        Mockito.doNothing().when(telegramChannelService).updateStatusById(anyLong(), any(TelegramChannelStatus.class));

        SendMessage actual = (SendMessage) administratorCommand.execute(update);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(ADMIN_ID.toString(), actual.getChatId());
        Assertions.assertEquals(MessageText.COMMAND_ADMINISTRATOR_CHANNEL_DELETED.getMessageText() + " - [-321]", actual.getText());

        Mockito.verify(telegramChannelService, Mockito.times(1)).updateStatusById(anyLong(), any(TelegramChannelStatus.class));
    }

    @Test
    void execute_shouldChangeUserIsBannedStatusToTrue_whenInputContainsBanParamAndUserId() {
        message.setText("/administrator ban -111");

        Mockito.doNothing().when(telegramUserService).updateIsBannedById(anyLong(), anyBoolean());

        SendMessage actual = (SendMessage) administratorCommand.execute(update);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(ADMIN_ID.toString(), actual.getChatId());
        Assertions.assertEquals(MessageText.COMMAND_ADMINISTRATOR_USER_BAN.getMessageText() + " - [-111]", actual.getText());

        Mockito.verify(telegramUserService, Mockito.times(1)).updateIsBannedById(anyLong(), anyBoolean());
    }

    @Test
    void execute_shouldChangeUserIsBannedStatusToFalse_whenInputContainsUnbanParamAndUserId() {
        message.setText("/administrator unban -111");

        Mockito.doNothing().when(telegramUserService).updateIsBannedById(anyLong(), anyBoolean());

        SendMessage actual = (SendMessage) administratorCommand.execute(update);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(ADMIN_ID.toString(), actual.getChatId());
        Assertions.assertEquals(MessageText.COMMAND_ADMINISTRATOR_USER_UNBAN.getMessageText() + " - [-111]", actual.getText());

        Mockito.verify(telegramUserService, Mockito.times(1)).updateIsBannedById(anyLong(), anyBoolean());
    }

    @Test
    void execute_shouldReturnAllActiveChannels_whenInputContainsChannelsParam() {
        TelegramChannel telegramChannel1 = new TelegramChannel(-345L, "https://example.com", TelegramChannelStatus.ACTIVE);
        TelegramChannel telegramChannel2 = new TelegramChannel(-678L, "https://test.com", TelegramChannelStatus.ACTIVE);

        message.setText("/administrator channels");

        Mockito.when(telegramChannelService.getAllWhereStatusIsActive())
                .thenReturn(List.of(telegramChannel1, telegramChannel2));

        SendMessage actual = (SendMessage) administratorCommand.execute(update);

        Assertions.assertTrue(actual.getText().contains(telegramChannel1.getId().toString()));
        Assertions.assertTrue(actual.getText().contains(telegramChannel1.getChannelLink()));
        Assertions.assertTrue(actual.getText().contains(telegramChannel2.getId().toString()));
        Assertions.assertTrue(actual.getText().contains(telegramChannel2.getChannelLink()));

        Mockito.verify(telegramChannelService, Mockito.times(1)).getAllWhereStatusIsActive();
    }

    @Test
    void execute_shouldReturnAllActiveUsersAlsoInactive_whenInputContainsUsersParam() {
        message.setText("/administrator users");

        TelegramUser telegramUser1 = new TelegramUser(1L, "jack", "member", true);
        TelegramUser telegramUser2 = new TelegramUser(2L, "john", "member", true);
        TelegramUser telegramUser3 = new TelegramUser(3L, "katy", "member", true);
        TelegramUser telegramUser4 = new TelegramUser(4L, "anna", "kicked", true);

        Set<TelegramUser> allUsers = Set.of(telegramUser1, telegramUser2, telegramUser3, telegramUser4);
        Set<TelegramUser> activeUsers = Set.of(telegramUser1, telegramUser2, telegramUser3);

        Mockito.when(telegramUserService.getAllActiveTelegramUsers()).thenReturn(activeUsers);
        Mockito.when(telegramUserService.getAll()).thenReturn(allUsers);

        SendMessage actual = (SendMessage) administratorCommand.execute(update);

        Assertions.assertNotNull(actual);
        Assertions.assertTrue(actual.getText().contains(
                MessageText.COMMAND_ADMINISTRATOR_ACTIVE_USERS_COUNT.getMessageText() + " - [" + activeUsers.size() + "]")
        );
        Assertions.assertTrue(actual.getText().contains(
                MessageText.COMMAND_ADMINISTRATOR_ALL_USERS_COUNT.getMessageText() + " - [" + allUsers.size() + "]"
        ));
    }

    @Test
    void execute_shouldReturnUnknownCommand_whenInputContainsInvalidParam() {
        message.setText("/administrator somethingForTest");

        try(MockedStatic<TelegramCustomLogger> mockedLogger = Mockito.mockStatic(TelegramCustomLogger.class)) {
            TelegramCustomLogger mockTelegramCustomLogger = Mockito.mock(TelegramCustomLogger.class);

            mockedLogger.when(TelegramCustomLogger::getInstance).thenReturn(mockTelegramCustomLogger);

            SendMessage actual = (SendMessage) administratorCommand.execute(update);

            Assertions.assertNotNull(actual);
            Assertions.assertEquals(MessageText.COMMAND_UNKNOWN.getMessageText(), actual.getText());
        }
    }

    @Test
    void execute_shouldReturnUnknownCommand_whenRequestNotSentByAdmin() {
        message.setText("/administrator channels");
        user.setId(777L);

        try(MockedStatic<TelegramCustomLogger> mockedLogger = Mockito.mockStatic(TelegramCustomLogger.class)) {
            TelegramCustomLogger mockTelegramCustomLogger = Mockito.mock(TelegramCustomLogger.class);

            mockedLogger.when(TelegramCustomLogger::getInstance).thenReturn(mockTelegramCustomLogger);

            SendMessage actual = (SendMessage) administratorCommand.execute(update);

            Assertions.assertNotNull(actual);
            Assertions.assertEquals(MessageText.COMMAND_UNKNOWN.getMessageText(), actual.getText());
        }
    }

    @Test
    void execute_shouldReturnUnknownCommand_whenInputContainsOnlyAdministratorCommand() {
        message.setText("/administrator");

        try(MockedStatic<TelegramCustomLogger> mockedLogger = Mockito.mockStatic(TelegramCustomLogger.class)) {
            TelegramCustomLogger mockTelegramCustomLogger = Mockito.mock(TelegramCustomLogger.class);

            mockedLogger.when(TelegramCustomLogger::getInstance).thenReturn(mockTelegramCustomLogger);

            SendMessage actual = (SendMessage) administratorCommand.execute(update);

            Assertions.assertNotNull(actual);
            Assertions.assertEquals(MessageText.COMMAND_UNKNOWN.getMessageText(), actual.getText());
        }
    }
}