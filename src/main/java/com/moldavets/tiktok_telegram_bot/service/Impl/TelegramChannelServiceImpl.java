package com.moldavets.tiktok_telegram_bot.service.Impl;

import com.moldavets.tiktok_telegram_bot.model.ConditionStatus;
import com.moldavets.tiktok_telegram_bot.model.Impl.TelegramChannel;
import com.moldavets.tiktok_telegram_bot.repository.TelegramChannelRepository;
import com.moldavets.tiktok_telegram_bot.service.TelegramChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TelegramChannelServiceImpl implements TelegramChannelService {

    private final TelegramChannelRepository telegramChannelRepository;

    @Autowired
    public TelegramChannelServiceImpl(TelegramChannelRepository telegramChannelRepository) {
        this.telegramChannelRepository = telegramChannelRepository;
    }

    @Override
    public TelegramChannel getById(Long id) {
        return telegramChannelRepository.findById(id).orElse(null);
    }

    @Override
    public List<TelegramChannel> getAllWhereStatusIsActive() {
        return telegramChannelRepository.findAllByStatus(ConditionStatus.ACTIVE);
    }
}
