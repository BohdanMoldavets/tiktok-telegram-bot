package com.moldavets.tiktok_telegram_bot.service.Impl;

import com.moldavets.tiktok_telegram_bot.logger.Impl.TelegramCustomLogger;
import com.moldavets.tiktok_telegram_bot.model.Impl.TelegramChannel;
import com.moldavets.tiktok_telegram_bot.model.TelegramChannelStatus;
import com.moldavets.tiktok_telegram_bot.repository.TelegramChannelRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
class TelegramChannelServiceImplTest {

    @Mock
    TelegramChannelRepository telegramChannelRepository;

    @InjectMocks
    TelegramChannelServiceImpl telegramChannelServiceImpl;

    private TelegramChannel telegramChannel1;
    private TelegramChannel telegramChannel2;
    private TelegramChannel telegramChannel3;

    @BeforeEach
    void setUp() {
        telegramChannel1 = new TelegramChannel(1L, "https://test.com/", TelegramChannelStatus.ACTIVE);
        telegramChannel2 = new TelegramChannel(2L, "https://example.com/", TelegramChannelStatus.ACTIVE);
        telegramChannel3 = new TelegramChannel(2L, "https://channel.com/", TelegramChannelStatus.INACTIVE);
    }

    @Test
    void getById_shouldReturnTelegramChannel_whenInputIdIsValid() {
        Mockito.when(telegramChannelRepository.findById(anyLong())).thenReturn(Optional.of(telegramChannel1));

        TelegramChannel storedChannel = telegramChannelServiceImpl.getById(1L);

        Assertions.assertEquals(telegramChannel1.getId(), storedChannel.getId());
        Assertions.assertEquals(telegramChannel1.getChannelLink(), storedChannel.getChannelLink());
        Assertions.assertEquals(telegramChannel1.getStatus(), storedChannel.getStatus());

        Mockito.verify(telegramChannelRepository, Mockito.times(1)).findById(anyLong());
    }

    @Test
    void getById_shouldReturnNull_whenInputIdIsNotStored() {
        Mockito.when(telegramChannelRepository.findById(anyLong())).thenReturn(Optional.empty());

        TelegramChannel storedChannel = telegramChannelServiceImpl.getById(1L);

        assertNull(storedChannel);

        Mockito.verify(telegramChannelRepository, Mockito.times(1)).findById(anyLong());
    }

    @Test
    void getAllWhereStatusIsActive_shouldReturnAllTelegramChannelsWhereStatusIsActive_whenAtLeastOneIsActive() {
        List<TelegramChannel> activeChannels = List.of(telegramChannel1, telegramChannel2);

        Mockito.when(telegramChannelRepository.findAllByStatus(TelegramChannelStatus.ACTIVE))
                .thenReturn(activeChannels);

        List<TelegramChannel> storedActiveChannels = telegramChannelServiceImpl.getAllWhereStatusIsActive();

        Assertions.assertEquals(activeChannels.size(), storedActiveChannels.size());

        Mockito.verify(telegramChannelRepository, Mockito.times(1)).findAllByStatus(TelegramChannelStatus.ACTIVE);
    }

    @Test
    void save_shouldSaveTelegramChannel_whenInputValidContainsValidData() {
        Mockito.when(telegramChannelRepository.save(any(TelegramChannel.class))).thenReturn(telegramChannel1);

        try (MockedStatic<TelegramCustomLogger> mockedLogger = Mockito.mockStatic(TelegramCustomLogger.class)) {
            TelegramCustomLogger telegramCustomLogger = Mockito.mock(TelegramCustomLogger.class);
            mockedLogger.when(TelegramCustomLogger::getInstance).thenReturn(telegramCustomLogger);

            telegramChannelServiceImpl.save(telegramChannel1);

            Mockito.verify(telegramChannelRepository, Mockito.times(1)).save(telegramChannel1);
        }
    }

    @Test
    void saveOrUpdate_shouldSaveTelegramChannel_whenInputContainsNotStoredTelegramChannel() {
        Mockito.when(telegramChannelRepository.findById(anyLong())).thenReturn(Optional.empty());
        Mockito.when(telegramChannelRepository.save(any(TelegramChannel.class))).thenReturn(telegramChannel1);

        try (MockedStatic<TelegramCustomLogger> mockedLogger = Mockito.mockStatic(TelegramCustomLogger.class)) {
            TelegramCustomLogger telegramCustomLogger = Mockito.mock(TelegramCustomLogger.class);
            mockedLogger.when(TelegramCustomLogger::getInstance).thenReturn(telegramCustomLogger);

            telegramChannelServiceImpl.saveOrUpdate(telegramChannel1);

            Mockito.verify(telegramChannelRepository, Mockito.times(1)).save(telegramChannel1);
        }
    }

    @Test
    void saveOrUpdate_shouldUpdateTelegramChannel_whenInputContainsStoredTelegramChannel() {
        Mockito.when(telegramChannelRepository.findById(anyLong())).thenReturn(Optional.of(telegramChannel1));
        Mockito.doNothing().when(telegramChannelRepository)
                .updateTelegramChannelById(anyLong(), anyString(), any(TelegramChannelStatus.class));

        try (MockedStatic<TelegramCustomLogger> mockedLogger = Mockito.mockStatic(TelegramCustomLogger.class)) {
            TelegramCustomLogger telegramCustomLogger = Mockito.mock(TelegramCustomLogger.class);
            mockedLogger.when(TelegramCustomLogger::getInstance).thenReturn(telegramCustomLogger);

            telegramChannelServiceImpl.saveOrUpdate(telegramChannel1);

            Mockito.verify(telegramChannelRepository, Mockito.times(1))
                    .updateTelegramChannelById(anyLong(), anyString(), any(TelegramChannelStatus.class));
        }
    }

    @Test
    void updateStatusById_shouldUpdateTelegramChannelStatus_whenInputDataIsValid() {
        Mockito.doNothing().when(telegramChannelRepository)
                .updateTelegramChannelStatusById(anyLong(), any(TelegramChannelStatus.class));

        try (MockedStatic<TelegramCustomLogger> mockedLogger = Mockito.mockStatic(TelegramCustomLogger.class)) {
            TelegramCustomLogger telegramCustomLogger = Mockito.mock(TelegramCustomLogger.class);
            mockedLogger.when(TelegramCustomLogger::getInstance).thenReturn(telegramCustomLogger);

            telegramChannelServiceImpl.updateStatusById(1L, TelegramChannelStatus.INACTIVE);

            Mockito.verify(telegramChannelRepository, Mockito.times(1))
                    .updateTelegramChannelStatusById(anyLong(), any(TelegramChannelStatus.class));
        }
    }
}

