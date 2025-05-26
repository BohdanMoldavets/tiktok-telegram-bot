package com.moldavets.tiktok_telegram_bot.service.Impl;

import com.moldavets.tiktok_telegram_bot.logger.Impl.TelegramCustomLogger;
import com.moldavets.tiktok_telegram_bot.model.Impl.TelegramUser;
import com.moldavets.tiktok_telegram_bot.model.TelegramUserStatus;
import com.moldavets.tiktok_telegram_bot.repository.TelegramUserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
class TelegramUserServiceImplTest {

    @Mock
    TelegramUserRepository telegramUserRepository;

    @InjectMocks
    TelegramUserServiceImpl telegramUserServiceImpl;

    @Test
    void getById_shouldReturnTelegramUser_whenInputContainsExistingTelegramUserId() {
        Mockito.when(telegramUserRepository.findById(anyLong()))
                .thenReturn(Optional.of(new TelegramUser(1L, "test", "member", true)));

        Long expected = 1L;
        Long actual = telegramUserServiceImpl.getById(1L).getId();

        assertEquals(expected, actual);
        Mockito.verify(telegramUserRepository, Mockito.times(1)).findById(anyLong());
    }

    @Test
    void getById_shouldReturnNull_whenInputContainsNotExistingTelegramUserId() {
        Mockito.when(telegramUserRepository.findById(anyLong())).thenReturn(Optional.empty());

        TelegramUser actual = telegramUserServiceImpl.getById(1L);

        assertNull(actual);
        Mockito.verify(telegramUserRepository, Mockito.times(1)).findById(anyLong());
    }

    @Test
    void getAll_shouldReturnAllTelegramUsers_whenTelegramUsersExist() {
        TelegramUser telegramUser1 = new TelegramUser(1L, "test", "member", true);
        TelegramUser telegramUser2 = new TelegramUser(2L, "test2", "member2", true);


        Mockito.when(telegramUserRepository.findAll()).thenReturn(Set.of(
                telegramUser1, telegramUser2
        ));


        Set<TelegramUser> actual = telegramUserServiceImpl.getAll();

        assertEquals(2, actual.size());
        Mockito.verify(telegramUserRepository, Mockito.times(1)).findAll();
    }

    @Test
    void getAll_shouldReturnEmptySet_whenTelegramUsersDoNotExist() {
        Mockito.when(telegramUserRepository.findAll()).thenReturn(Set.of());

        Set<TelegramUser> actual = telegramUserServiceImpl.getAll();

        assertEquals(0, actual.size());
        Mockito.verify(telegramUserRepository, Mockito.times(1)).findAll();
    }

    @Test
    void getAllActiveTelegramUsers_shouldReturnAllActiveTelegramUsers_whenActiveTelegramUsersExist() {
        TelegramUser telegramUser1 = new TelegramUser(1L, "test", "member", true);
        TelegramUser telegramUser2 = new TelegramUser(2L, "test2", "member2", true);

        Mockito.when(telegramUserRepository.findAllByStatusIsNotIn(anySet()))
                .thenReturn(Set.of(telegramUser1, telegramUser2));

        Set<TelegramUser> actual = telegramUserServiceImpl.getAllActiveTelegramUsers();

        assertEquals(2, actual.size());
        Mockito.verify(telegramUserRepository, Mockito.times(1)).findAllByStatusIsNotIn(anySet());
    }

    @Test
    void getAllActiveTelegramUsers_shouldReturnEmptySet_whenActiveTelegramUsersDoNotExist() {
        Mockito.when(telegramUserRepository.findAllByStatusIsNotIn(anySet()))
                .thenReturn(Set.of());

        Set<TelegramUser> actual = telegramUserServiceImpl.getAllActiveTelegramUsers();

        assertEquals(0, actual.size());
        Mockito.verify(telegramUserRepository, Mockito.times(1)).findAllByStatusIsNotIn(anySet());
    }

    @Test
    void checkTelegramUserRegistration_shouldSaveTelegramUser_whenInputContainsNotStoredTelegramUser() {
        TelegramUser telegramUser = new TelegramUser(1L, "test", "member", true);

        Mockito.when(telegramUserRepository.findById(anyLong())).thenReturn(Optional.empty());
        Mockito.when(telegramUserRepository.save(Mockito.any(TelegramUser.class))).thenReturn(telegramUser);

        try (MockedStatic<TelegramCustomLogger> mockedLogger = Mockito.mockStatic(TelegramCustomLogger.class)) {
            TelegramCustomLogger telegramCustomLogger = Mockito.mock(TelegramCustomLogger.class);
            mockedLogger.when(TelegramCustomLogger::getInstance).thenReturn(telegramCustomLogger);

            telegramUserServiceImpl.checkTelegramUserRegistration(1L, "test");

            Mockito.verify(telegramUserRepository, Mockito.times(1)).findById(anyLong());
            Mockito.verify(telegramUserRepository, Mockito.times(1)).save(Mockito.any(TelegramUser.class));
        }
    }

    @Test
    void checkTelegramUserRegistration_shouldUpdateTelegramUserStatusToMember_whenInputContainsStoredTelegramUserWhoseStatusIsKicked() {
        TelegramUser telegramUser = new TelegramUser(1L, "test", "kicked", true);

        Mockito.when(telegramUserRepository.findById(anyLong())).thenReturn(Optional.of(telegramUser));
        Mockito.doNothing().when(telegramUserRepository).updateTelegramUserStatusById(anyLong(), anyString());

        try (MockedStatic<TelegramCustomLogger> mockedLogger = Mockito.mockStatic(TelegramCustomLogger.class)) {
            TelegramCustomLogger telegramCustomLogger = Mockito.mock(TelegramCustomLogger.class);
            mockedLogger.when(TelegramCustomLogger::getInstance).thenReturn(telegramCustomLogger);

            telegramUserServiceImpl.checkTelegramUserRegistration(1L, "test");

            Mockito.verify(telegramUserRepository, Mockito.times(1)).findById(anyLong());
            Mockito.verify(telegramUserRepository, Mockito.times(1)).updateTelegramUserStatusById(anyLong(), anyString());
        }
    }

    @Test
    void checkTelegramUserRegistration_shouldDoNothingWhenTelegramUserStatusAlreadyMember_whenInputContainsStoredTelegramUser() {
        TelegramUser telegramUser = new TelegramUser(1L, "test", "member", true);

        Mockito.when(telegramUserRepository.findById(anyLong())).thenReturn(Optional.of(telegramUser));
        
        telegramUserServiceImpl.checkTelegramUserRegistration(1L, "test");

        Mockito.verify(telegramUserRepository, Mockito.times(1)).findById(anyLong());
        Mockito.verify(telegramUserRepository, Mockito.times(0)).updateTelegramUserStatusById(anyLong(), anyString());
    }

    @Test
    void save_shouldSaveTelegramUser_whenInputContainsValidTelegramUser() {
        TelegramUser telegramUser = new TelegramUser(1L, "test", "member", true);
        Mockito.when(telegramUserRepository.save(any(TelegramUser.class))).thenReturn(telegramUser);

        try (MockedStatic<TelegramCustomLogger> mockedLogger = Mockito.mockStatic(TelegramCustomLogger.class)) {
            TelegramCustomLogger telegramCustomLogger = Mockito.mock(TelegramCustomLogger.class);
            mockedLogger.when(TelegramCustomLogger::getInstance).thenReturn(telegramCustomLogger);

            telegramUserServiceImpl.save(telegramUser);

            Mockito.verify(telegramUserRepository, Mockito.times(1)).save(any(TelegramUser.class));
        }
    }

    @Test
    void updateStatusById_shouldUpdateTelegramUserStatus_whenInputContainsValidData() {
        Mockito.doNothing().when(telegramUserRepository).updateTelegramUserStatusById(anyLong(), anyString());

        try (MockedStatic<TelegramCustomLogger> mockedLogger = Mockito.mockStatic(TelegramCustomLogger.class)) {
            TelegramCustomLogger telegramCustomLogger = Mockito.mock(TelegramCustomLogger.class);
            mockedLogger.when(TelegramCustomLogger::getInstance).thenReturn(telegramCustomLogger);

            telegramUserServiceImpl.updateStatusById(1L, TelegramUserStatus.KICKED);

            Mockito.verify(telegramUserRepository, Mockito.times(1)).updateTelegramUserStatusById(anyLong(), anyString());
        }
    }

    @Test
    void updateSubscriptionForAllUsers_shouldUpdateSubscriptionForAllUsers_whenInputContainsValidData() {
        Mockito.doNothing().when(telegramUserRepository).updateAllTelegramUsersSubscription(anyBoolean());

        try (MockedStatic<TelegramCustomLogger> mockedLogger = Mockito.mockStatic(TelegramCustomLogger.class)) {
            TelegramCustomLogger telegramCustomLogger = Mockito.mock(TelegramCustomLogger.class);
            mockedLogger.when(TelegramCustomLogger::getInstance).thenReturn(telegramCustomLogger);

            telegramUserServiceImpl.updateSubscriptionForAllUsers(false);

            Mockito.verify(telegramUserRepository, Mockito.times(1)).updateAllTelegramUsersSubscription(anyBoolean());
        }
    }

    @Test
    void updateSubscribeById_shouldUpdateSubscribe_whenInputContainsValidData() {
        Mockito.doNothing().when(telegramUserRepository).updateTelegramUserIsSubscribedById(anyLong(), anyBoolean());

        try (MockedStatic<TelegramCustomLogger> mockedLogger = Mockito.mockStatic(TelegramCustomLogger.class)) {
            TelegramCustomLogger telegramCustomLogger = Mockito.mock(TelegramCustomLogger.class);
            mockedLogger.when(TelegramCustomLogger::getInstance).thenReturn(telegramCustomLogger);

            telegramUserServiceImpl.updateSubscribeById(1L, true);

            Mockito.verify(telegramUserRepository, Mockito.times(1)).updateTelegramUserIsSubscribedById(anyLong(), anyBoolean());
        }
    }

    @Test
    void updateLastModifiedDateById_shouldUpdateLastModifiedDate_whenInputContainsValidData() {
        Mockito.doNothing().when(telegramUserRepository).updateTelegramUserLastModifiedDateById(anyLong(), any(Instant.class));

        try (MockedStatic<TelegramCustomLogger> mockedLogger = Mockito.mockStatic(TelegramCustomLogger.class)) {
            TelegramCustomLogger telegramCustomLogger = Mockito.mock(TelegramCustomLogger.class);
            mockedLogger.when(TelegramCustomLogger::getInstance).thenReturn(telegramCustomLogger);

            telegramUserServiceImpl.updateLastModifiedDateById(1L);

            Mockito.verify(telegramUserRepository, Mockito.times(1)).updateTelegramUserLastModifiedDateById(anyLong(), any(Instant.class));
        }
    }

    @Test
    void updateIsBannedById_shouldUpdateIsBanned_whenInputContainsValidData() {
        Mockito.doNothing().when(telegramUserRepository).updateTelegramUserIsBannedById(anyLong(), anyBoolean());

        try (MockedStatic<TelegramCustomLogger> mockedLogger = Mockito.mockStatic(TelegramCustomLogger.class)) {
            TelegramCustomLogger telegramCustomLogger = Mockito.mock(TelegramCustomLogger.class);
            mockedLogger.when(TelegramCustomLogger::getInstance).thenReturn(telegramCustomLogger);

            telegramUserServiceImpl.updateIsBannedById(1L, true);

            Mockito.verify(telegramUserRepository, Mockito.times(1)).updateTelegramUserIsBannedById(anyLong(), anyBoolean());
        }
    }
}