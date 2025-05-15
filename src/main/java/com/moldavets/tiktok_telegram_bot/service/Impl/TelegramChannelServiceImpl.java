package com.moldavets.tiktok_telegram_bot.service.Impl;

import com.moldavets.tiktok_telegram_bot.model.Impl.TelegramChannel;
import com.moldavets.tiktok_telegram_bot.repository.TelegramChannelRepository;
import com.moldavets.tiktok_telegram_bot.service.TelegramChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TelegramChannelServiceImpl implements TelegramChannelService {

    private final TelegramChannelRepository telegramChannelRepository;

    @Override
    public TelegramChannel getById(Long id) {
        return telegramChannelRepository.findById(id).orElse(null);
    }
}
